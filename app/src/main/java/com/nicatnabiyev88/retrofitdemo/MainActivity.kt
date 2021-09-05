package com.nicatnabiyev88.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var retService: AlbumService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retService = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)

        //getRequestWithQueryParameters()
        //getRequestWithPathParameters()
        uploadAlbum()
    }

    private fun getRequestWithQueryParameters(){
        val responseLiveData: LiveData<Response<Albums>> = liveData {
            val response = retService.getSortedAlbums(7)
            emit(response)
        }
        responseLiveData.observe(this, Observer {
            val albumsList = it.body()?.listIterator()
            if (albumsList!=null){
                while (albumsList.hasNext()){
                    val albumsItem = albumsList.next()
                    val result = " Album Title: ${albumsItem.title} \n" +
                            " Album ID: ${albumsItem.id} \n" +
                            " User ID: ${albumsItem.userId} \n\n"
                    text_view.append(result)
                }
            }
        })
    }

    private fun getRequestWithPathParameters(){
        val pathResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.getAlbum(5)
            emit(response)
        }
        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(this, title,Toast.LENGTH_SHORT).show()
        })
    }

    private fun uploadAlbum(){
        val album = AlbumsItem(0,"My Title", 7)
        val postResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.uploadAlbum(album)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val recievedAlbumsItem = it.body()
            val result = " Album Title: ${recievedAlbumsItem?.title} \n" +
                    " Album ID: ${recievedAlbumsItem?.id} \n" +
                    " User ID: ${recievedAlbumsItem?.userId} \n\n"
            text_view.text = result
        })
    }
}


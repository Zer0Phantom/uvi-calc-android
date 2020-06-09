package de.baumanngeorg.uvilsfrechner.config

import android.content.Context
import com.android.volley.Cache
import com.android.volley.Network
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack

object InternetResourceLoader {
    private var queue: RequestQueue? = null

    fun addRequest(request: Request<*>?, context: Context?) {
        if(queue == null){
            queue = getQueue(context)
        }
        queue!!.add(request)
    }

    private fun getQueue(context: Context?): RequestQueue {
        val cache: Cache = DiskBasedCache(context!!.cacheDir, 1024 * 1024)
        val network: Network = BasicNetwork(HurlStack())
        val queue = RequestQueue(cache, network)
        queue.start()
        return queue
    }
}
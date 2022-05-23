package com.softkare.itreader.fragments

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.softkare.itreader.R


class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val pdfurl = intent.getStringExtra("pdf_url")
        println(pdfurl)
        val pdf = findViewById<WebView>(R.id.web)
        pdf.webChromeClient = object : WebChromeClient(){}
        pdf.webViewClient = object : WebViewClient(){}
        pdf.settings.setSupportZoom(true)
        pdf.settings.javaScriptEnabled = true
        if (pdfurl != null) {
            pdf.loadUrl(pdfurl)
        }

        pdf.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimeType)
            request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading file...")
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, ".pdf")
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(applicationContext, "Downloading File", Toast.LENGTH_LONG).show()
        }

    }

    /*RetrivePDFfromUrl(pdf).execute(pdfurl)
}
    // create an async task class for loading pdf file from URL.
    // create an async task class for loading pdf file from URL.
private class RetrivePDFfromUrl(pdf: PDFView) : AsyncTask<String?, Void?, InputStream?>() {
        var pdf2 = pdf
        protected override fun doInBackground(vararg strings: String?): InputStream? {
            // we are using inputstream
            // for getting out PDF.
            var inputStream: InputStream? = null
            val url = URL(strings[0])
            // below is the step where we are
            // creating our connection.
            val urlConnection: HttpURLConnection =
                url.openConnection() as HttpsURLConnection
            if (urlConnection.getResponseCode() === 200) {
                // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = BufferedInputStream(urlConnection.getInputStream())
            }
            return inputStream
        }

        public override fun onPostExecute(inputStream: InputStream?) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdf2.fromStream(inputStream).load()
        }
}*/

}
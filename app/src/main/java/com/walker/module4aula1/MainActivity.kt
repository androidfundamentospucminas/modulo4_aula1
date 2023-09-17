package com.walker.module4aula1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView

const val TEXT_MESSAGE = "TEXT_MESSAGE"

class MainActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var executeButton: Button
    private lateinit var cleanButton: Button
    private lateinit var scrollContainer: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultText = findViewById(R.id.resultText)
        executeButton = findViewById(R.id.executeButton)
        cleanButton = findViewById(R.id.cleanButton)
        scrollContainer = findViewById(R.id.scrollContainer)

        executeButton.setOnClickListener {
            // executeJobWithHandler()
            executeJobWithThread()
        }

        cleanButton.setOnClickListener {
            cleanContent()
        }
    }

    private fun executeJobWithHandler() {

        val handler = Handler(Looper.getMainLooper())

        val runnable = Runnable { writeText("Handler:: Texto do Runnable sem delay") }
        handler.post(runnable)

        val runnable2 = Runnable { writeText("Handler::Texto do Runnable com delay de 1000") }
        handler.postDelayed(runnable2, 1000)

        val runnable3 = Runnable { writeText("Handler::Texto do Runnable com delay de 2000") }
        handler.postDelayed(runnable3, 2000)

        writeText("Handler:: Texto síncrono 1")
        writeText("Handler:: Texto síncrono 2")
    }

    private fun executeJobWithThread() {

        val handler = object: Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                val bundle = msg.data
                writeText(bundle.getString(TEXT_MESSAGE) ?: "")
            }
        }

        val runnable = Runnable {
            for (i in 1..12) {
                val text = "Texto da Thread - Contador: $i"
                Log.d("Thread::", text)

                // Vai falhar porque não pode rodar thread background e alterar a UI
                writeText(text)


                Message().apply {
                    this.data = Bundle().apply {
                        putString(TEXT_MESSAGE, text)
                    }
                    handler.sendMessage(this)
                }

                Thread.sleep(1000)
            }
        }

        
        Thread(runnable).start()


        writeText("Texto síncrono 1")
        writeText("Texto síncrono 2")
    }

    private fun writeText(text: String) {
        resultText.append("$text\n")
        scrollContainer.fullScroll(ScrollView.FOCUS_DOWN)
    }

    private fun cleanContent() {
        resultText.text = ""
        scrollContainer.fullScroll(ScrollView.FOCUS_UP)
    }
}
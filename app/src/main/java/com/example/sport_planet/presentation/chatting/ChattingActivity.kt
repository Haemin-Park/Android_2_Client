package com.example.sport_planet.presentation.chatting

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sport_planet.R
import com.example.sport_planet.databinding.ActivityChattingBinding
import com.example.sport_planet.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_chatting.*

class ChattingActivity : BaseActivity<ActivityChattingBinding>(R.layout.activity_chatting) {

    private val chattingAdapter = ChattingAdapter(this)
    val chattingViewModel: ChattingViewModel = ChattingViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rv_activity_chatting_recyclerview.run {
            adapter = chattingAdapter
            layoutManager = LinearLayoutManager(this@ChattingActivity)
            setHasFixedSize(true)
        }

        chattingViewModel.settingStomp()
        chattingViewModel.ChattingMessageLiveData.observe(this,
                Observer {
                    chattingAdapter.addChattingMessage(it)
                }
        )

        bt_activity_chatting_send.setOnClickListener{
            chattingViewModel.sendMessage(et_activity_chatting_message_content.text.toString())
            et_activity_chatting_message_content.text = null
        }



    }
}
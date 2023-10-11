package com.example.grandpa

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.grandpa.databinding.PhonePopBinding

class DetailPhonePopDialog(context : Context) :Dialog(context){
    private lateinit var binding: PhonePopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PhonePopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        // 빈 화면 터치를 통해 dialog가 사라지지 않도록
        setCancelable(false)

        phoneBack.setOnClickListener {
            dismiss()
        }
    }
}
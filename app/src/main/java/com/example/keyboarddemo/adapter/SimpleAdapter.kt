package com.example.keyboarddemo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.keyboarddemo.R

class SimpleAdapter:BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_keyboard) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.setText(R.id.title,item)

    }
}
package com.example.mytools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.mytools.R

class SimpleAdapter:BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_keyboard) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.setText(R.id.title,item)

    }
}
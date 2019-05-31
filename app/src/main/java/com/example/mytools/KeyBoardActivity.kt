package com.example.mytools

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import com.example.mytools.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.activity_keyboard.*


class KeyBoardActivity : AppCompatActivity() {
    val r = Rect()
    var viewbottom = 0
    var fullDisplay:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)
        val inputLayout = LayoutInflater.from(this).inflate(R.layout.layout_input, null)
        val headview = LayoutInflater.from(this).inflate(R.layout.item_foot, recycler.parent as ViewGroup,false)
//        inputLayout.visibility = View.GONE
        findViewById<FrameLayout>(android.R.id.content).addView(
            inputLayout,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM
            })
        recycler.getViewTreeObserver().addOnGlobalLayoutListener(OnGlobalLayoutListener //当键盘弹出隐藏的时候会 调用此方法。
        {

            //获取当前界面可视部分(键盘和虚拟键盘  不是可视化部分)
            window.decorView.getWindowVisibleDisplayFrame(r)
            //获取屏幕的高度
            val screenHeight = window.decorView.rootView.height//1920
            val displayHeight = r.bottom//1974
            //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
            if(fullDisplay==null){
                fullDisplay = displayHeight
            }
            val keyBoardHeight = screenHeight - displayHeight
            Log.d("Keyboard", "键盘和虚拟键一起的高度: ${keyBoardHeight}")
            if (displayHeight == fullDisplay) {
                //说明键盘已经隐藏
                if(inputLayout.visibility!=View.GONE){
                    inputLayout.visibility = View.GONE
                    headview.visibility = View.GONE
                }
            } else {
                if(inputLayout.visibility!=View.VISIBLE){
                    inputLayout.visibility = View.VISIBLE
                    headview.visibility = View.VISIBLE
                    (inputLayout.layoutParams as FrameLayout.LayoutParams).apply {
                        bottomMargin = keyBoardHeight-(screenHeight-fullDisplay!!)
                    }

                    val rect = IntArray(2)
                    inputLayout.getLocationOnScreen(rect)
                    Log.d("Keyboard", "输入框到屏幕底部距离${1920-rect[1]}")
//                val scrolly =(keyBoardHeight + inputLayout.height)-( screenHeight - viewbottom)
                    val scrolly =(viewbottom-rect[1])
                    Log.d("Keyboard", "item 应该滚动${scrolly}")
                                    recycler.smoothScrollBy(0,viewbottom-(displayHeight-inputLayout.height))
                    if(!inputLayout.findViewById<EditText>(R.id.input).isFocused){
                        inputLayout.postDelayed({
                            inputLayout.findViewById<EditText>(R.id.input).requestFocus()
                        },100)
                    }
                }
            }
        })
//        recycler.setVerticalScrollBarEnabled(false)
        recycler.adapter = SimpleAdapter().apply {
            setNewData(Array<String>(100, {
                it.toString()
            }).asList())
            addFooterView(headview)
            setOnItemClickListener { adapter, view, position ->
                val rect  =  Rect()
                view.getGlobalVisibleRect(rect)
                viewbottom = rect.bottom//当前item 到屏幕顶部距离   屏幕高度是1920
                Log.d("Keyboard", "item 到屏幕底部距离 ${1920-viewbottom}")
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            }
        }
    }

}
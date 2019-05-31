package com.example.mytools

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import com.example.mytools.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.activity_keyboard.*
import android.graphics.Rect
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout


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
        addContentView(
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
            val scrolly =(keyBoardHeight + inputLayout.height)-( screenHeight - viewbottom)

            Log.d("Keyboard", "Size: ${scrolly}")
            if (displayHeight == fullDisplay) {
                //说明键盘已经隐藏
                inputLayout.visibility = View.GONE
                headview.visibility = View.GONE
            } else {
                headview.visibility = View.VISIBLE
                inputLayout.visibility = View.VISIBLE
                (inputLayout.layoutParams as FrameLayout.LayoutParams).apply {
                    bottomMargin = keyBoardHeight-(screenHeight-fullDisplay!!)
                }
//                val rect  =  Rect()
//                inputLayout.getGlobalVisibleRect(rect)
//                val scrolly =viewbottom-rect.top
                recycler.scrollBy(0,scrolly)
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
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                (recycler.layoutManager as LinearLayoutManager).sc
            }
        }
    }

}
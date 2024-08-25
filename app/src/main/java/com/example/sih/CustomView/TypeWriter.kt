package com.example.sih.CustomView

import android.content.Context
import android.os.Handler
import android.util.AttributeSet

class TypeWriter : androidx.appcompat.widget.AppCompatTextView {
    private var mText: CharSequence? =null
    private var mIndex = 0
    private var mDelay: Long= 500
    private val mHandler = Handler()
    private val charecterAdder: Runnable= object :Runnable{
        override fun run() {
            text=mText!!.subSequence(0,mIndex++)
            if(mIndex<=mText!!.length){
                mHandler.postDelayed(this,mDelay)
            }
        }
    }
    constructor(context: Context?): super(context!!)
    constructor(context: Context?,attrs: AttributeSet?): super(context!!,attrs)
    fun animateText(text: CharSequence){
        mText= text
        mIndex= 0
        setText("")
        mHandler.removeCallbacks(charecterAdder)
        mHandler.postDelayed(charecterAdder,mDelay)
    }
    fun setCharacterDelay(millis: Long){
        mDelay= millis
    }
}
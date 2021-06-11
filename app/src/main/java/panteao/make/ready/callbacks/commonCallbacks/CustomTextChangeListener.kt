package panteao.make.ready.callbacks.commonCallbacks

import android.text.Editable
import android.text.TextWatcher

open class CustomTextChangeListener(private val mListener: TextChangeListener) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        mListener.onTextChanged(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable) {

    }

    interface TextChangeListener {
        fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
    }
}

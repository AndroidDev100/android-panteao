package panteao.make.ready.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import panteao.make.ready.R
import panteao.make.ready.databinding.ResponseDialogFragmentBinding
import panteao.make.ready.utils.constants.AppConstants

class ResponseDialogFragment : DialogFragment() {


    private lateinit var responseDialogFragmentBinding: ResponseDialogFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        responseDialogFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.response_dialog_fragment, container, false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent);
        return responseDialogFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val bundle = arguments
        var message = ""
        if (bundle != null) {
            message = bundle.getString(AppConstants.ERROR_MESSAGE) as String
            responseDialogFragmentBinding.txtResponse.text = message
        } else {
            responseDialogFragmentBinding.txtResponse.text = getString(R.string.username_password_doest_match)
        }

        responseDialogFragmentBinding.btnCancel.setOnClickListener {
            dismiss()
        }
    }


    companion object {
        fun newInstance(): ResponseDialogFragment {
            return ResponseDialogFragment()
        }
    }

}
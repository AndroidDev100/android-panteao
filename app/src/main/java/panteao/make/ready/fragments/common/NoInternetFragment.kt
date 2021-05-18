package panteao.make.ready.fragments.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import panteao.make.ready.R
import panteao.make.ready.databinding.NoInternetConnectionBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NoInternetFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_network_settings -> {
                startActivityForResult(Intent(Settings.ACTION_WIFI_SETTINGS), 0)
            }
            R.id.retry_txt -> {
                listener?.onFragmentInteraction()
            }
        }
    }

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<NoInternetConnectionBinding>(
            inflater,
            R.layout.no_internet_connection,
            container,
            false
        )
//        var imageView = view.findViewById<ImageView>(R.id.loadingImage)
        binding.buttonNetworkSettings.setOnClickListener(this)
        binding.buttonNetworkSettings.requestFocus()
        binding.retryTxt.setOnClickListener(this)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction()
    }

}

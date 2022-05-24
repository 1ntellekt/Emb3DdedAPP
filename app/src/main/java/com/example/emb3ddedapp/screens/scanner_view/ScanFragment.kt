package com.example.emb3ddedapp.screens.scanner_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.*
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.ScanFragmentBinding
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.VIEWER_WEB_PAGE
import com.example.emb3ddedapp.utils.showToast

class ScanFragment : Fragment() {

    private lateinit var binding:ScanFragmentBinding
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScanFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            codeScanner = CodeScanner(requireContext(), scannerView)

            // Parameters (default values)
            codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
            // ex. listOf(BarcodeFormat.QR_CODE)
            codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
            codeScanner.isAutoFocusEnabled = false // Whether to enable auto focus or not
            codeScanner.isFlashEnabled = false // Whether to enable flash or not

            // Callbacks
            codeScanner.decodeCallback = DecodeCallback { result->
                activity?.runOnUiThread {
                    if (result.text.startsWith(VIEWER_WEB_PAGE)){
                        val bundle = Bundle()
                        bundle.putString("urlModel", result.text)
                        APP.mNavController.navigate(R.id.action_scanFragment_to_webViewFragment, bundle)
                    } else {
                        showToast("Scanned: ${result.text}")
                    }
                }
            }
            codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                activity?.runOnUiThread{
                    showToast("Camera initialization error: ${it.message}")
                }
            }

            scannerView.setOnClickListener {
                codeScanner.startPreview()
            }
        }
    }

}
package com.vt.vt.ui.bottom_navigation.scan

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.zxing.Result
import com.king.zxing.CameraScan
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.DefaultCameraScan
import com.king.zxing.analyze.MultiFormatAnalyzer
import com.vt.vt.R
import com.vt.vt.databinding.FragmentScanBinding
import com.vt.vt.ui.ToDetailAfterScanFragment
import com.vt.vt.utils.PermissionUtils
import com.vt.vt.utils.PermissionUtils.showPermissionRationaleDialog

class ScanFragment : Fragment(), CameraScan.OnScanResultCallback {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private var mCameraScan: CameraScan? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasPermission(requireContext())) {
            requestResultLauncher.launch(REQUIRED_PERMISSION)
        } else {
            initCameraScan()
            startCameraScan()
        }
    }

    override fun onDestroy() {
        releaseCamera()
        super.onDestroy()
        _binding = null
    }

    private fun initCameraScan() {
        mCameraScan = DefaultCameraScan(this, binding.previewCamera)
        (mCameraScan as DefaultCameraScan).setOnScanResultCallback(this)
    }

    private fun startCameraScan() {
        //初始化解码配置
        val decodeConfig = DecodeConfig()
        decodeConfig.apply {
            hints = DecodeFormatManager.QR_CODE_HINTS
            areaRectRatio = 0.8f
            isFullAreaScan = false
        }
        mCameraScan?.apply {
            setVibrate(true)
            setPlayBeep(true)
            setAnalyzer(MultiFormatAnalyzer(decodeConfig))
            setAnalyzeImage(true)
        }
        mCameraScan?.startCamera()
    }

    private val requestResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            var permissionGranted = true
            permission.entries.forEach {
                if (it.key in REQUIRED_PERMISSION && !it.value) {
                    permissionGranted = false
                }
            }
            if (!permissionGranted) {
                showPermissionRationaleDialog(requireContext())
                Toast.makeText(requireActivity(), "Permission Request Denied", Toast.LENGTH_SHORT)
                    .show()
            } else {
                startCameraScan()
            }
        }

    private fun releaseCamera() {
        mCameraScan?.release()
    }

    override fun onScanResultCallback(result: Result?): Boolean {
        val bundle = Bundle()
        bundle.putString(ToDetailAfterScanFragment.EXTRA_SCAN_ID, result?.text)
        view?.findNavController()?.navigate(R.id.action_navigation_scan_to_toDetailAfterScanFragment, bundle)
        return true
    }

    companion object {
        private val REQUIRED_PERMISSION = mutableListOf(
            android.Manifest.permission.CAMERA
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()

        fun hasPermission(context: Context) = REQUIRED_PERMISSION.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

}
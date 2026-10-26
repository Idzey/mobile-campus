package com.example.intentshowcase

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.intentshowcase.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val margin = (24 * resources.displayMetrics.density).toInt()
            view.setPadding(bars.left + margin, bars.top + margin, bars.right + margin, bars.bottom + margin)
            insets
        }

        binding.textMessage.text = intent.getStringExtra(EXTRA_MESSAGE).orEmpty()

        // Тип запуска определяем по реально полученному интенту:
        // у явного интента action не задан, у неявного он равен ACTION_SHOW_MESSAGE.
        binding.textKind.setText(
            if (intent.action == ACTION_SHOW_MESSAGE) R.string.kind_implicit else R.string.kind_explicit
        )

        binding.buttonBack.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_MESSAGE = "com.example.intentshowcase.extra.MESSAGE"
        const val ACTION_SHOW_MESSAGE = "com.example.intentshowcase.action.SHOW_MESSAGE"
    }
}

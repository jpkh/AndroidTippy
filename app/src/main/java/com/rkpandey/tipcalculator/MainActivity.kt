package com.rkpandey.tipcalculator

import android.animation.ArgbEvaluator
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var tvFooter: TextView

    private lateinit var tvSplitPeopleLabel: TextView
    private lateinit var tvSplitValueLabel: TextView
    private lateinit var tvSplitValue: TextView
    private lateinit var etSplitPeople: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialise and retrieve all references
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        tvFooter = findViewById(R.id.tvFooter)
        tvSplitPeopleLabel = findViewById(R.id.tvSplitPeopleLabel)
        tvSplitValueLabel = findViewById(R.id.tvSplitValueLabel)
        tvSplitValue = findViewById(R.id.tvSplitValue)
        etSplitPeople = findViewById(R.id.etSplitPeople)

        // Initialise slider values
        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        // Check if seekBar position has changed
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text = "$progress%"
                updateTipDescription(progress)
                computeTipAndTotal()
            }

            // Ignore, just for sanity
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            // Ignore, just for sanity
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Check and react if etBaseAmount has changes
        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }
        })

        // Check and react if etBaseAmount has changes
        etSplitPeople.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }
        })


        tvFooter.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/rpandey1234")))
        }
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        // Check if etBaseAmouht is empty, return if it is or we crash
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        // Get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        // Compute the tip and update the UI
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        tvTipAmount.text = "$%.2f".format(tipAmount)
        tvTotalAmount.text = "$%.2f".format(totalAmount)

        // Check if splitPeople has value
        if(etSplitPeople.text.isNotEmpty()) {
            val splitValue = totalAmount / etSplitPeople.text.toString().toDouble()
            tvSplitValue.text = "$%.2f".format(splitValue)
            tvSplitValueLabel.visibility = View.VISIBLE
            tvSplitValue.visibility = View.VISIBLE
        } else {
            tvSplitValue.text = ""
            tvSplitValueLabel.visibility = View.INVISIBLE
            tvSplitValue.visibility = View.INVISIBLE
        }


    }
}

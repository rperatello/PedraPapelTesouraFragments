package br.edu.ifsp.scl.sdm.pa2.pedrapapeltesourafragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ifsp.scl.sdm.pa2.pedrapapeltesourafragments.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        supportFragmentManager?.beginTransaction().add(R.id.mainFcv, MainFragment(), "MainFragment").commit()

    }
}
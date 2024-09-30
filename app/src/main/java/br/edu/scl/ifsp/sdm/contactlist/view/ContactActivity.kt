package br.edu.scl.ifsp.sdm.contactlist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {
        private val acb: ActivityContactBinding by lazy {
            ActivityContactBinding.inflate(layoutInflater)
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(acb.root)

    }
}
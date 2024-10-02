package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class ContactActivity : AppCompatActivity() {
        private val acb: ActivityContactBinding by lazy {
            ActivityContactBinding.inflate(layoutInflater)
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        setSupportActionBar(acb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_details)

        //adicionar um listenet ao botão save. Criei com o with pois vou ter que acessar vários elementos da view
        with(acb){
            saveBt.setOnClickListener {
                //criar um contato com os dados que foram preenchidos no formulário
                val contact = Contact(
                    id = hashCode(),
                    name = nameEt.text.toString(),
                    address = addressEt.text.toString(),
                    phone = phoneEt.text.toString(),
                    email = emailEt.text.toString()
                )
                //criar um intent para retornar o contato para a activity que chamou essa
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_CONTACT, contact)
                }
                //setResult é um método que seta o resultado da activity. O primeiro parametro é o resultado (RESULT_OK é um
                //resultado padrão do android que indica que a operação foi bem sucedida), e o segundo é o intent que vai
                //retornar o contato
                setResult(RESULT_OK, resultIntent)
                //finish é um método que finaliza a activity
                finish()
            }
        }
    }
}
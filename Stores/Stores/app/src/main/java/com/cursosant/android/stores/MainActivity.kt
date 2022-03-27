package com.cursosant.android.stores

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.cursosant.android.stores.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener, MainAux {  //implementa la interface del adaptador OnClickListener.

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager   //Configura el layout para que sea una grilla.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)  
        setContentView(mBinding.root)

        mBinding.fab.setOnClickListener { launchEditFragment() } //crear instancia del fragmento

        setupRecylcerView()
    }

    private fun launchEditFragment(args: Bundle? = null) {
        val fragment = EditStoreFragment()
        if (args != null) fragment.arguments = args

        val fragmentManager = supportFragmentManager //gestor para controlar fragmentos
        val fragmentTransaction = fragmentManager.beginTransaction() //decide como se va a ejecutar

        fragmentTransaction.add(R.id.containerMain, fragment) //Indicar contenedor de fragmento y fragmento.
        fragmentTransaction.addToBackStack(null) //para retroceder del fragmento a la main activity,
        fragmentTransaction.commit() //que se guarden los cambiso

        hideFab()
    }

    private fun setupRecylcerView() {
        mAdapter = StoreAdapter(mutableListOf(), this) //recibe 2 parámetros, arreglo vacío y el listener, this porque ya se implementa listeener
        mGridLayout = GridLayoutManager(this, 2) //this = contexto para pasar la actividdad, 2 cantidad de columnas
        getStores()

        mBinding.recyclerView.apply { //configuración del recyclerView
            setHasFixedSize(true) //indicar que no cambia de tamañao y optimizar recursos
            layoutManager = mGridLayout
            adapter = mAdapter  //el m diferencia entre variable globar y propiedad
        }
    }

    private fun getStores(){ //consulta usando ANKO
        doAsync { //Evita que la consulta se ejecute en primer plano.
            val stores = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                mAdapter.setStores(stores) //cuando este terminada se seteará en el adaptador.
            }
        }
    }

    /*
    * OnClickListener
    * */
    override fun onClick(storeId: Long) { //cuando clickee una tienda, que abra el fragmento para poder editarlo
        val args = Bundle()
        args.putLong(getString(R.string.arg_id), storeId)

        launchEditFragment(args)
    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {  //Lógica del check de tienda
        storeEntity.isFavorite = !storeEntity.isFavorite
        doAsync { //actualización con ANKO a la db
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                updateStore(storeEntity)
            }
        }
    }

    override fun onDeleteStore(storeEntity: StoreEntity) { //comportamiento al presionar borrar
        val items = resources.getStringArray(R.array.array_options_item) //arreglo de opciones

        MaterialAlertDialogBuilder(this) //Modal de consulta si quiere borrar
                .setTitle(R.string.dialog_options_title)
                .setItems(items, { dialogInterface, i ->
                    when(i){
                        0 -> confirmDelete(storeEntity)

                        1 -> dial(storeEntity.phone)

                        2 -> goToWebsite(storeEntity.website)
                    }
                })
                .show()
    }

    private fun confirmDelete(storeEntity: StoreEntity){
        MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_delete_title)
                .setPositiveButton(R.string.dialog_delete_confirm, { dialogInterface, i ->
                    doAsync {
                        StoreApplication.database.storeDao().deleteStore(storeEntity)
                        uiThread {
                            mAdapter.delete(storeEntity)
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_delete_cancel, null)
                .show()
    }

    private fun dial(phone: String){  //Abrir la opción para llamar.
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:$phone")
        }

        startIntent(callIntent)
    }

    private fun goToWebsite(website: String){  //Acción cuando seleccionas vas a la web de la tienda
        if (website.isEmpty()){
            Toast.makeText(this, R.string.main_error_no_website, Toast.LENGTH_LONG).show()  //Si el campo url de la web es vacio, que muetre un mensaje.
        } else {
            val websiteIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(website)
            }

            startIntent(websiteIntent)
        }
    }

    private fun startIntent(intent: Intent){
        if (intent.resolveActivity(packageManager) != null)
            startActivity(intent)
        else
            Toast.makeText(this, R.string.main_error_no_resolve, Toast.LENGTH_LONG).show()
    }

    /*
    * MainAux
    * */
    override fun hideFab(isVisible: Boolean) {
        if (isVisible) mBinding.fab.show() else mBinding.fab.hide()
    }

    override fun addStore(storeEntity: StoreEntity) {
        mAdapter.add(storeEntity)
    }

    override fun updateStore(storeEntity: StoreEntity) {
        mAdapter.update(storeEntity)
    }
}
package com.example.listecourse.Activity.Recette;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listecourse.Activity.ListeDeCourse.ContenueListeDeCourse;
import com.example.listecourse.Activity.ListeDeCourse.ListeDeCourseActivity;
import com.example.listecourse.Activity.Produit.ProduitActivity;
import com.example.listecourse.Entity.Produit;
import com.example.listecourse.Entity.Produit_recette;
import com.example.listecourse.Entity.Recette;
import com.example.listecourse.Entity.Unite;
import com.example.listecourse.R;
import com.example.listecourse.Tools.DatabaseLinker;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AjoutProduitActivity extends AppCompatActivity {
    private TableLayout containerProduit;
    private Spinner spinnerProduit;
    private Spinner spinnerUnite;
    private DatabaseLinker databaseManager;
    private EditText editVolume;
    private EditText editQuantite;
    private Unite unite;
    private int idRecette;
    private int idProduitRecette;
    private Recette recette;
    private Produit produitSelectionner;
    private Produit_recette produitRecette;
    private ArrayAdapter<Produit> adapterProduit;
    private ArrayAdapter<Unite> adapterUnite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.produit_manager_recette);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editVolume = findViewById(R.id.edit_volume);
        editQuantite = findViewById(R.id.edit_quantite);
        databaseManager = new DatabaseLinker( this );

        spinnerProduit();
        spinnerUnite();

        Intent intent = this.getIntent();
        idRecette = intent.getIntExtra("idRecette", 0);
        idProduitRecette = intent.getIntExtra("idProduitRecette", 0);
        if(idProduitRecette != 0){
            setValueOfProduit();
        }

        try {
            Dao<Recette, Integer> daoRecette = databaseManager.getDao( Recette.class );
            recette = daoRecette.queryForId(idRecette);

            this.getSupportActionBar().setTitle(recette.getLibelle());
        } catch (java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }


        ButtonCreate();
    }

    private void setValueOfProduit(){
        try {
            Dao<Produit_recette, Integer> daoProduit = databaseManager.getDao( Produit_recette.class );
            produitRecette = daoProduit.queryForId(idProduitRecette);
            if(produitRecette.getUnite().getLibelle().equals("Unité")){
                editVolume.setText("");
            }else{
                editVolume.setText(String.valueOf(produitRecette.getVolume()));
            }
            for(int cpt = 0 ; cpt < spinnerProduit.getCount(); cpt++){
                if(String.valueOf(adapterProduit.getItem(cpt).getLibelle()).equals(produitRecette.getProduit().getLibelle())){
                    spinnerProduit.setSelection(cpt);
                }
            }
            for(int cpt = 0 ; cpt < spinnerUnite.getCount(); cpt++){
                if(String.valueOf(adapterUnite.getItem(cpt).getLibelle()).equals(produitRecette.getUnite().getLibelle())){
                    spinnerUnite.setSelection(cpt);
                }
            }

            editQuantite.setText(String.valueOf(produitRecette.getQuantite()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void ButtonCreate() {
        Button buttonCreateProduitRecette = findViewById(R.id.button_validate);
        buttonCreateProduitRecette.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    Dao<Produit_recette, Integer> daoProduitRecette = databaseManager.getDao(Produit_recette.class);
                    unite =(Unite) spinnerUnite.getSelectedItem();
                    if(idProduitRecette == 0){
                        produitRecette = new Produit_recette();
                    }
                    produitRecette.setRecette(recette);
                    produitRecette.setProduit(produitSelectionner);
                    produitRecette.setQuantite(Integer.valueOf(editQuantite.getText().toString()));
                    if(editVolume.getText().toString().equals("")){
                        produitRecette.setVolume(0);
                    }else{
                        produitRecette.setVolume(Float.valueOf(editVolume.getText().toString()));
                    }
                    produitRecette.setUnite(unite);
                    if(idProduitRecette == 0){
                        daoProduitRecette.create(produitRecette);
                    }else
                    {
                        daoProduitRecette.update(produitRecette);
                    }
                    Intent monIntent = new Intent(AjoutProduitActivity.this, ContenueRecetteActivity.class);
                    monIntent.putExtra("idRecette", idRecette) ;
                    startActivity(monIntent);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    private void spinnerProduit(){
        List<Produit> listeProduit = new ArrayList<>();
        try {
            Dao<Produit, Integer> daoProduit = databaseManager.getDao( Produit.class );
            listeProduit = daoProduit.queryForAll();
        } catch (java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        spinnerProduit = findViewById(R.id.spinner_produit);
        //arrayAdapter prend les objets pour les intégrés au spinner
        adapterProduit = new ArrayAdapter<Produit>(
                this,
                android.R.layout.simple_spinner_item,
                listeProduit);

        spinnerProduit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler(parent, view, position, id);
                produitSelectionner = (Produit)  spinnerProduit.getSelectedItem();;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapterProduit.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerProduit.setAdapter(adapterProduit);

    }

    private void spinnerUnite(){
        List<Unite> listeUnite = new ArrayList<>();
        try {
            Dao<Unite, Integer> daoUnite = databaseManager.getDao( Unite.class );
            listeUnite = daoUnite.queryForAll();
        } catch (java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        spinnerUnite = findViewById(R.id.spinner_unite);
        //arrayAdapter prend les objets pour les intégrés au spinner
        adapterUnite = new ArrayAdapter<Unite>(
                this,
                android.R.layout.simple_spinner_item,
                listeUnite);

        spinnerUnite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler(parent, view, position, id);
                Unite unite = (Unite) spinnerUnite.getSelectedItem();
                if(unite.getLibelle().equals("Unité")){
                    editVolume.getBackground().setAlpha(50);
                    editVolume.setText("");
                    editVolume.setFocusable(View.NOT_FOCUSABLE);
                }else if(!editVolume.isFocusable()){
                    editVolume.getBackground().setAlpha(250);
                    editVolume.setFocusableInTouchMode(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapterUnite.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerUnite.setAdapter(adapterUnite);

        for(int cpt = 0 ; cpt < spinnerUnite.getCount(); cpt++){
            if(String.valueOf(adapterUnite.getItem(cpt).getLibelle()).equals("Unité")){
                spinnerUnite.setSelection(cpt);
            }
        }
    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
    }

    public boolean onOptionsItemSelected(MenuItem item){
        backToMenu();
        return true;
    }

    public void backToMenu(){
        Intent monIntent = new Intent(AjoutProduitActivity.this, ContenueRecetteActivity.class);
        monIntent.putExtra("idRecette", idRecette) ;
        startActivity(monIntent);
    }
}

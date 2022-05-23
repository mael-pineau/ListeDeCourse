package com.example.listecourse.Activity.ListeDeCourse;

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
import com.example.listecourse.Entity.ListeCourse;
import com.example.listecourse.Entity.Produit;
import com.example.listecourse.Entity.Produit_listeCourse;
import com.example.listecourse.Entity.Produit_recette;
import com.example.listecourse.Entity.Recette;
import com.example.listecourse.Entity.Unite;
import com.example.listecourse.R;
import com.example.listecourse.Tools.DatabaseLinker;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AjoutRecetteActivity extends AppCompatActivity{

    private DatabaseLinker databaseManager;
    private int idListe;
    private ListeCourse listeCourse;
    private Spinner spinnerRecette;
    private Recette recetteSelectionner;
    private ArrayAdapter<Recette> adapterRecette;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_recette_liste);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        idListe = intent.getIntExtra("idListe", 0);


        databaseManager = new DatabaseLinker( this );
        try {
            Dao<ListeCourse, Integer> daoListeCourse = databaseManager.getDao( ListeCourse.class );
            listeCourse = daoListeCourse.queryForId(idListe);

            this.getSupportActionBar().setTitle(listeCourse.getLibelle());
        } catch (java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        spinnerRecette();

        ButtonCreate();
    }

    private void ButtonCreate() {
        Button buttonCreateProduitRecette = findViewById(R.id.button_validate);
        buttonCreateProduitRecette.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    Dao<Produit_recette, Integer> daoProduitRecette = databaseManager.getDao(Produit_recette.class);
                    Recette recette =(Recette) spinnerRecette.getSelectedItem();

                    List<Produit_recette> listProduitRecette =
                            daoProduitRecette.query(
                                    daoProduitRecette.queryBuilder().where()
                                            .eq("recette_id", recette.getId())
                                            .prepare());

                    for (Produit_recette produitRecette : listProduitRecette){
                        Produit_listeCourse nouveauProduitListeCourse = new Produit_listeCourse();
                        nouveauProduitListeCourse.setTicked(false);
                        nouveauProduitListeCourse.setListeCourse(listeCourse);
                        nouveauProduitListeCourse.setProduit(produitRecette.getProduit());
                        nouveauProduitListeCourse.setQuantite(produitRecette.getQuantite());
                        nouveauProduitListeCourse.setUnite(produitRecette.getUnite());
                        nouveauProduitListeCourse.setVolume(produitRecette.getVolume());

                        Dao<Produit_listeCourse, Integer> daoProduitListeCourse = databaseManager.getDao(Produit_listeCourse.class);
                        daoProduitListeCourse.create(nouveauProduitListeCourse);
                    }



                    Intent monIntent = new Intent(AjoutRecetteActivity.this, ContenueListeDeCourse.class);
                    monIntent.putExtra("idListe", idListe) ;
                    startActivity(monIntent);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    private void spinnerRecette(){
        List<Recette> listeRecette = new ArrayList<>();
        try {
            Dao<Recette, Integer> daoRecette = databaseManager.getDao( Recette.class );
            listeRecette = daoRecette.queryForAll();
        } catch (java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        spinnerRecette = findViewById(R.id.spinner_recette);
        //arrayAdapter prend les objets pour les intégrés au spinner
        adapterRecette = new ArrayAdapter<Recette>(
                this,
                android.R.layout.simple_spinner_item,
                listeRecette);

        spinnerRecette.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler(parent, view, position, id);
                recetteSelectionner = (Recette)  spinnerRecette.getSelectedItem();;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapterRecette.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerRecette.setAdapter(adapterRecette);

    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
    }

    public boolean onOptionsItemSelected(MenuItem item){
        backToMenu();
        return true;
    }

    public void backToMenu(){
        Intent monIntent = new Intent(AjoutRecetteActivity.this, ContenueListeDeCourse.class);
        monIntent.putExtra("idListe", idListe) ;
        startActivity(monIntent);
    }
}

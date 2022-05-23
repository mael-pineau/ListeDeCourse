package com.example.listecourse.Activity.ListeDeCourse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.listecourse.Entity.Produit;
import com.example.listecourse.Entity.Produit_listeCourse;
import com.example.listecourse.Tools.DatabaseLinker;
import com.example.listecourse.Entity.ListeCourse;
import com.example.listecourse.R;
import com.j256.ormlite.dao.Dao;

import java.util.List;

public class ContenueListeDeCourse extends AppCompatActivity{
    private Dao<ListeCourse, Integer> daoListeCourse;
    private LinearLayout linearLayoutButton;
    private TableLayout containerListe;
    private Button buttonAjoutProduitByRecette;
    private Button buttonAjoutProduit;
<<<<<<< HEAD
    private int idListe;
    private ListeCourse listeCourse;
=======
    private String idListe;
>>>>>>> 36ab7a13eeccc5218cfaa0023a7cb49a63ad4f17

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contenu_liste);

        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

<<<<<<< HEAD
        Intent intent = this.getIntent();
        idListe = intent.getIntExtra("idListe", 0);
=======
        //// TODO: 13/04/2022
        Intent i = this.getIntent();
        idListe = i.getStringExtra("idListe");
        buttonAjoutProduitByRecette = findViewById(R.id.addProduitTolisteByRecette);
        buttonAjoutProduitByRecette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContenueListeDeCourse.this, ProduitRecetteToListCours.class);
                i.putExtra("idListe", idListe);
                startActivity(i);

            }
        });
>>>>>>> 36ab7a13eeccc5218cfaa0023a7cb49a63ad4f17

        DatabaseLinker databaseManager = new DatabaseLinker( this );
        try {
            Dao<ListeCourse, Integer> daoListeCourse = databaseManager.getDao( ListeCourse.class );
            listeCourse = daoListeCourse.queryForId(idListe);

            this.getSupportActionBar().setTitle(listeCourse.getLibelle());
        } catch (java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        containerListe = findViewById(R.id.liste);
        buttonAjoutProduit = findViewById(R.id.button);

        buttonAjoutProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent monIntent = new Intent(ContenueListeDeCourse.this, AjoutProduitActivity.class);
                monIntent.putExtra("idListe", idListe) ;
                startActivity(monIntent);
            }
        });

        buttonAjoutProduitByRecette = findViewById(R.id.addProduitTolisteByRecette);
        buttonAjoutProduitByRecette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent monIntent = new Intent(ContenueListeDeCourse.this, AjoutRecetteActivity.class);
                monIntent.putExtra("idListe", idListe) ;
                startActivity(monIntent);
            }
        });


        createListeCourse();
    }


    private void createListeCourse() {
        containerListe.removeAllViews();

        DatabaseLinker databaseManager = new DatabaseLinker( this );
        try {
            Dao<ListeCourse, Integer> daoListeCourse = databaseManager.getDao( ListeCourse.class );
            Dao<Produit_listeCourse, Integer> daoProduit_listeCourse = databaseManager.getDao( Produit_listeCourse.class );
            List<Produit_listeCourse> listeCourses =
                    daoProduit_listeCourse.query(
                            daoProduit_listeCourse.queryBuilder().where()
                                    .eq("listeCourse_id", idListe)
                                    .prepare());
            for (Produit_listeCourse liste : listeCourses) {
                Produit produit_recette = liste.getProduit();

                TableRow row = new TableRow(this);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setWeightSum(8);

                TableRow.LayoutParams param = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        4f
                );


                if(liste.getUnite().getLibelle().equals("Unité")){
                    TextView labelQuantite = new TextView(this);
                    labelQuantite.setLayoutParams(param);
                    labelQuantite.setText(String.valueOf(liste.getQuantite()));
                    row.addView(labelQuantite);
                }else{
                    TextView labelVolume = new TextView(this);
                    labelVolume.setLayoutParams(param);
                    labelVolume.setText(String.valueOf(liste.getVolume())+" "+liste.getUnite().getLibelle());
                    row.addView(labelVolume);
                }


                TextView labelNom = new TextView(this);
                labelNom.setLayoutParams(param);
                labelNom.setText(produit_recette.getLibelle());
                row.addView(labelNom);




                TableRow.LayoutParams paramButton = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f
                );



                ImageButton deleteClient = new ImageButton(this);
                deleteClient.setLayoutParams(paramButton);
                deleteClient.setImageResource(R.drawable.delete);
                deleteClient.setBackground(null);
                row.addView(deleteClient);
                deleteClient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            daoProduit_listeCourse.delete(liste);
                        } catch (SQLException | java.sql.SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        ((ViewGroup) row.getParent()).removeView(row);
                    }
                });

                containerListe.addView(row);
            }
        } catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        backToMenu();
        return true;
    }

    public void backToMenu(){
        Intent retour = new Intent(ContenueListeDeCourse.this , ListeDeCourseActivity.class);
        startActivity(retour);
    }
}

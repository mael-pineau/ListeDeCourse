package com.example.listecourse.Activity.Produit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listecourse.Activity.ListeDeCourse.ListeDeCourseActivity;
import com.example.listecourse.Activity.MainActivity;
import com.example.listecourse.Activity.Recette.RecetteActivity;
import com.example.listecourse.Entity.Produit;
import com.example.listecourse.Entity.Produit_listeCourse;
import com.example.listecourse.Entity.Produit_recette;
import com.example.listecourse.Entity.Recette;
import com.example.listecourse.Tools.DatabaseLinker;
import com.example.listecourse.Entity.ListeCourse;
import com.example.listecourse.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class ProduitActivity extends AppCompatActivity{
    private Dao<ListeCourse, Integer> daoListeCourse;
    private TableLayout containerListe;
    private Button buttonAjoutListe;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produit);
        this.getSupportActionBar().setTitle("Produit");

        //on force l'appli à etre en light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //recupération du bottomView
        bottomNavigationView = findViewById(R.id.bottom_menu);
        //definition des actions des bouttons
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // switch pour que les bouttons nous fasse changer de d'activité
                        switch (item.getItemId()) {
                            case R.id.liste_de_course:
                                Intent liste = new Intent(ProduitActivity.this, ListeDeCourseActivity.class);
                                startActivity(liste);
                                break;
                            case R.id.produit:
                                break;
                            case R.id.recette:
                                Intent recette = new Intent(ProduitActivity.this, RecetteActivity.class);
                                startActivity(recette);
                                break;
                        }
                        return false;
                    }
                });
        //récupération du conteneur
        containerListe = findViewById(R.id.liste_de_course);
        //récupération du boutton de création d'un produit
        buttonAjoutListe = findViewById(R.id.button_recette);
        //on définie l'action du produit
        buttonAjoutListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //création d'un espace de dialogue
                final EditText input = new EditText(ProduitActivity.this);
                //création d'un alert dialogue
                AlertDialog alertDialog = new AlertDialog.Builder(ProduitActivity.this).create();
                alertDialog.setTitle("Nouveau produit :");
                //ajout de l'espace de dialogue à l'alert dialogue
                alertDialog.setView(input, 50, 0, 50, 0); // 10 spacing, left and right
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //création du produit avec les nom données par l'utilisateur.
                        DatabaseLinker linker = new DatabaseLinker( ProduitActivity.this );
                        Dao<Produit, Integer> daoProduit = null;
                        try {
                            daoProduit = linker.getDao( Produit.class );
                        } catch (java.sql.SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        String name = input.getText().toString();
                        Produit produit = new Produit();
                        produit.setLibelle(name);

                        try {
                            daoProduit.create(produit);
                        } catch (java.sql.SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        //on recharge l'activité pour quel affiche le nouveau produit
                        Intent monIntent = new Intent(ProduitActivity.this, ProduitActivity.class);
                        startActivity(monIntent);
                    }
                });
                alertDialog.show();

            }
        });

        createListeCourse();
    }

    private void createListeCourse() {
        //on efface tous les produit afficher du container
        containerListe.removeAllViews();
        //on récupère tous les produits
        DatabaseLinker databaseManager = new DatabaseLinker( this );
        try {
            Dao<Produit, Integer> daoProduit = databaseManager.getDao( Produit.class );
            List<Produit> listProduit = daoProduit.queryForAll();
            //on crée un ligne par produit
            for (Produit liste : listProduit) {
                TableRow row = new TableRow(this);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setWeightSum(8);

                TableRow.LayoutParams param = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        4f
                );
                //création du label contenant le nom du produit
                TextView labelNom = new TextView(this);
                labelNom.setLayoutParams(param);
                labelNom.setText(liste.getLibelle());
                row.addView(labelNom);

                TableRow.LayoutParams paramButton = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f
                );


                //création du boutton de supression du produit
                ImageButton deleteClient = new ImageButton(this);
                deleteClient.setLayoutParams(paramButton);
                deleteClient.setImageResource(R.drawable.delete);
                deleteClient.setBackground(null);
                row.addView(deleteClient);
                deleteClient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //création d'un alertdialog pour la confirmation de suppression de ce produit.
                        AlertDialog alertDialog = new AlertDialog.Builder(ProduitActivity.this).create(); //Read Update
                        alertDialog.setTitle("Supression");
                        alertDialog.setMessage("Êtes vous sur de vouloir supprimer ce produit ?");

                        alertDialog.setButton("Supprimer", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String nameListe = "";
                                List<Produit_recette> produit_recette = new ArrayList<>();
                                List<Produit_listeCourse> produit_liste = new ArrayList<>();
                                try {

                                    //supression du produit dans les recettes
                                    Dao<Produit_recette, Integer> daoProduit_recette = databaseManager.getDao(Produit_recette.class);
                                    produit_recette =
                                            daoProduit_recette.query(
                                                    daoProduit_recette.queryBuilder().where()
                                                            .eq("produit_id", liste.getId())
                                                            .prepare());
                                    for(Produit_recette produit_voulu : produit_recette){
                                        daoProduit_recette.delete(produit_voulu);
                                    }
                                    //suppression du produit dans les listes de courses
                                    Dao<Produit_listeCourse, Integer> daoProduit_listeCourse = databaseManager.getDao(Produit_listeCourse.class);
                                    produit_liste =
                                            daoProduit_listeCourse.query(
                                                    daoProduit_listeCourse.queryBuilder().where()
                                                            .eq("listeCourse_id", liste.getId())
                                                            .prepare());
                                    for(Produit_listeCourse produit_voulu_2 : produit_liste){
                                        daoProduit_listeCourse.delete(produit_voulu_2);
                                    }
                                    //récupération nom du produit
                                    nameListe = liste.getLibelle();
                                    //suppression du produit
                                    daoProduit.delete(liste);
                                } catch (SQLException | java.sql.SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                ((ViewGroup) row.getParent()).removeView(row);
                                //affichage d'un toast pour montrer que le produit à été supprimer.
                                Toast.makeText(getApplicationContext(), nameListe +" supprimer",Toast.LENGTH_LONG).show();
                            }
                        });
                        alertDialog.show();  //<-- Affichage de l'alerDialog
                    }
                });

                containerListe.addView(row);
            }
        } catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}

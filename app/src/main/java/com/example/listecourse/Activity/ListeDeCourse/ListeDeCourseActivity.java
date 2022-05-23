package com.example.listecourse.Activity.ListeDeCourse;

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

import com.example.listecourse.Activity.MainActivity;
import com.example.listecourse.Activity.Produit.ProduitActivity;
import com.example.listecourse.Activity.Recette.RecetteActivity;
import com.example.listecourse.Entity.Produit;
import com.example.listecourse.Entity.Produit_listeCourse;
import com.example.listecourse.Entity.Produit_recette;
import com.example.listecourse.Entity.Recette;
import com.example.listecourse.Entity.Unite;
import com.example.listecourse.Tools.DatabaseLinker;
import com.example.listecourse.Entity.ListeCourse;
import com.example.listecourse.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class ListeDeCourseActivity extends AppCompatActivity{
    private Dao<ListeCourse, Integer> daoListeCourse;
    private TableLayout containerListe;
    private Button buttonAjoutListe;
    private BottomNavigationView bottomNavigationView;
    private DatabaseLinker linker;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //suppression de la bdd
        //this.deleteDatabase("listeCourse.db");
        //testBDD();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_de_course);
        this.getSupportActionBar().setTitle("Liste de Course");

        // Set light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        linker = new DatabaseLinker(this);
        //recupération du bottomView
        bottomNavigationView = findViewById(R.id.bottom_menu);
        //definition des actions des bouttons
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.liste_de_course:
                                break;
                            case R.id.produit:
                                Intent produit = new Intent(ListeDeCourseActivity.this, ProduitActivity.class);
                                startActivity(produit);
                                break;
                            case R.id.recette:
                                Intent recette = new Intent(ListeDeCourseActivity.this, RecetteActivity.class);
                                startActivity(recette);
                                break;
                        }
                        return false;
                    }
                });

        containerListe = findViewById(R.id.liste_de_course);
        buttonAjoutListe = findViewById(R.id.button_recette);

        buttonAjoutListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(ListeDeCourseActivity.this);
                AlertDialog alertDialog = new AlertDialog.Builder(ListeDeCourseActivity.this).create();
                alertDialog.setTitle("Nouvelle Liste de Course :");
                alertDialog.setView(input, 50, 0, 50, 0); // 10 spacing, left and right
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseLinker linker = new DatabaseLinker( ListeDeCourseActivity.this );
                        Dao<ListeCourse, Integer> daoListeCourse = null;
                        try {
                            daoListeCourse = linker.getDao( ListeCourse.class );
                        } catch (java.sql.SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        String name = input.getText().toString();
                        ListeCourse listeCourse = new ListeCourse();
                        listeCourse.setLibelle(name);

                        try {
                            daoListeCourse.create(listeCourse);
                        } catch (java.sql.SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        Intent monIntent = new Intent(ListeDeCourseActivity.this, ListeDeCourseActivity.class);
                        startActivity(monIntent);
                    }
                });
                alertDialog.show();

            }
        });

        createListeCourse();
    }

    private void createListeCourse() {
        containerListe.removeAllViews();

        DatabaseLinker databaseManager = new DatabaseLinker( this );
        try {
            Dao<ListeCourse, Integer> daoListeCourse = databaseManager.getDao( ListeCourse.class );
            List<ListeCourse> listeCourses = daoListeCourse.queryForAll();

            for (ListeCourse liste : listeCourses) {
                TableRow row = new TableRow(this);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setWeightSum(8);



                TableRow.LayoutParams param = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        4f
                );

                TextView labelNom = new TextView(this);
                labelNom.setLayoutParams(param);
                labelNom.setText(liste.getLibelle());
                row.addView(labelNom);

                TableRow.LayoutParams paramButton = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f
                );

                ImageButton editClient = new ImageButton(this);
                editClient.setLayoutParams(paramButton);
                editClient.setBackground(null);
                editClient.setImageResource(R.drawable.edit);
                row.addView(editClient);
                editClient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText input = new EditText(ListeDeCourseActivity.this);
                        AlertDialog alertDialog = new AlertDialog.Builder(ListeDeCourseActivity.this).create();
                        alertDialog.setTitle("Modifier nom Liste de Course :");
                        alertDialog.setView(input, 50, 0, 50, 0); // 10 spacing, left and right
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseLinker linker = new DatabaseLinker( ListeDeCourseActivity.this );
                                Dao<ListeCourse, Integer> daoListeCourse = null;
                                try {
                                    daoListeCourse = linker.getDao( ListeCourse.class );
                                } catch (java.sql.SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                String name = input.getText().toString();
                                liste.setLibelle(name);
                                try {
                                    daoListeCourse.update(liste);
                                } catch (java.sql.SQLException throwables) {
                                    throwables.printStackTrace();
                                }

                                Intent monIntent = new Intent(ListeDeCourseActivity.this, ListeDeCourseActivity.class);
                                startActivity(monIntent);
                            }
                        });
                        alertDialog.show();

                    }

                });

                ImageButton ajoutRecette = new ImageButton(this);
                ajoutRecette.setLayoutParams(paramButton);
                ajoutRecette.setBackground(null);
                ajoutRecette.setImageResource(R.drawable.add);
                row.addView(ajoutRecette);
                ajoutRecette.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dao<Recette, Integer> daoRecette = null;
                        try {
                            daoRecette = linker.getDao( Recette.class );
                        } catch (java.sql.SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        ajoutRecette.setImageTintMode(PorterDuff.Mode.DARKEN);

                        int idList = liste.getId();

                        Intent i = new Intent(ListeDeCourseActivity.this, ContenueListeDeCourse.class);
                        i.putExtra("idListe", idList);
                        startActivity(i);
                    }
                });

                ImageButton deleteClient = new ImageButton(this);
                deleteClient.setLayoutParams(paramButton);
                deleteClient.setImageResource(R.drawable.delete);
                deleteClient.setBackground(null);
                row.addView(deleteClient);
                deleteClient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog alertDialog = new AlertDialog.Builder(ListeDeCourseActivity.this).create(); //Read Update
                        alertDialog.setTitle("Supression");
                        alertDialog.setMessage("Êtes vous sur de vouloir supprimer cette liste ?");

                        alertDialog.setButton("Supprimer", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String nameListe = "";
                                try {
                                    nameListe = liste.getLibelle();
                                    daoListeCourse.delete(liste);
                                } catch (SQLException | java.sql.SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                ((ViewGroup) row.getParent()).removeView(row);

                                Toast.makeText(getApplicationContext(), nameListe +" supprimer",Toast.LENGTH_LONG).show();
                            }
                        });

                        alertDialog.show();  //<-- See This!
                    }
                });

                containerListe.addView(row);
            }
        } catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void testBDD(){
        DatabaseLinker databaseManager = new DatabaseLinker(this);

        List<Produit> listProduit = new ArrayList<>();
        List<Unite> listUnite = new ArrayList<>();
        List<Recette> listRecette = new ArrayList<>();
        List<ListeCourse> listListCourse = new ArrayList<>();

        List<Produit_recette> listProduit_recette = new ArrayList<>();
        List<Produit_listeCourse> listProduit_listeCourse = new ArrayList<>();

        List<Produit_recette> listProduit_recette_crepes = new ArrayList<>();
        List<Produit_listeCourse> listProduit_listeCourseLundi = new ArrayList<>();
        ListeCourse listeCourseLundi = null;

        try {
            Dao<Produit, Integer> daoProduit = databaseManager.getDao(Produit.class);
            Dao<Recette, Integer> daoRecette = databaseManager.getDao(Recette.class);
            Dao<Unite, Integer> daoUnite = databaseManager.getDao(Unite.class);
            Dao<ListeCourse, Integer> daoListeCourse = databaseManager.getDao(ListeCourse.class);

            Dao<Produit_recette, Integer> daoProduit_recette = databaseManager.getDao(Produit_recette.class);
            Dao<Produit_listeCourse, Integer> daoProduit_listeCourse = databaseManager.getDao(Produit_listeCourse.class);

            listProduit = daoProduit.queryForAll();
            listUnite = daoUnite.queryForAll();
            listRecette = daoRecette.queryForAll();
            listListCourse = daoListeCourse.queryForAll();

            listProduit_recette = daoProduit_recette.queryForAll();
            listProduit_listeCourse = daoProduit_listeCourse.queryForAll();

            Recette crepes = daoRecette.queryForId(2);
            listeCourseLundi = daoListeCourse.queryForId(1);

            listProduit_recette_crepes =
                    daoProduit_recette.query(
                            daoProduit_recette.queryBuilder().where()
                                    .eq("recette_id", crepes.getId())
                                    .prepare());

            listProduit_listeCourseLundi =
                    daoProduit_listeCourse.query(
                            daoProduit_listeCourse.queryBuilder().where()
                                    .eq("listeCourse_id", listeCourseLundi.getId())
                                    .prepare());



        } catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        Log.i("-----------", "---------------------------------------------");

        for (Produit produit : listProduit) {
            Log.i("", "J'ai toruvé un produit");
            Log.i("Son libelle est ", produit.getLibelle());
        }

        Log.i("-----------", "---------------------------------------------");

        for (Unite unite : listUnite) {
            Log.i("", "J'ai toruvé une unite");
            Log.i("Son libelle est ", unite.getLibelle());
        }

        Log.i("-----------", "---------------------------------------------");

        for (Recette recette : listRecette) {
            Log.i("", "J'ai toruvé une recette");
            Log.i("Son libelle est ", recette.getLibelle());
        }

        Log.i("-----------", "---------------------------------------------");

        for (Produit_recette produit_recette : listProduit_recette_crepes) {
            Log.i("", "J'ai toruvé un produit de la recette crepes");
            Log.i("", produit_recette.getProduit().getLibelle() + " "+produit_recette.getVolume() + " "+ produit_recette.getUnite().getLibelle());
            Log.i("Quantite du produit", String.valueOf(produit_recette.getQuantite()));
        }

        Log.i("-----------", "---------------------------------------------");

        Log.i("liste course lundi", "");
        Log.i("Son nom :", listeCourseLundi.getLibelle());

        for (Produit_listeCourse produit_listeCourse : listProduit_listeCourseLundi) {
            Log.i("produit trouvé", produit_listeCourse.getProduit().getLibelle() + " "+produit_listeCourse.getVolume()+ " "+produit_listeCourse.getUnite().getLibelle());
            Log.i("Quantite du produit", String.valueOf(produit_listeCourse.getQuantite()));
        }

        for (Produit_recette produit_recette : listProduit_recette_crepes) {
            Log.i("", produit_recette.getProduit().getLibelle() + " "+produit_recette.getVolume() + " "+ produit_recette.getUnite().getLibelle());
            Log.i("Quantite du produit", String.valueOf(produit_recette.getQuantite()));
        }
    }

}

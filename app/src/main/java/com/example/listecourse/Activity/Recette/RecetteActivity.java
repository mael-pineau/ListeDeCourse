package com.example.listecourse.Activity.Recette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.PorterDuff;
import android.os.Bundle;
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

import com.example.listecourse.Activity.ListeDeCourse.ContenueListeDeCourse;
import com.example.listecourse.Activity.ListeDeCourse.ListeDeCourseActivity;
import com.example.listecourse.Activity.MainActivity;
import com.example.listecourse.Activity.Produit.ProduitActivity;
import com.example.listecourse.Entity.Produit_recette;
import com.example.listecourse.Entity.Recette;
import com.example.listecourse.Tools.DatabaseLinker;
import com.example.listecourse.Entity.ListeCourse;
import com.example.listecourse.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class RecetteActivity extends AppCompatActivity{
    private Dao<ListeCourse, Integer> daoListeCourse;
    private TableLayout containerListe;
    private Button buttonAjoutListe;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recette);
        this.getSupportActionBar().setTitle("Recette");

        containerListe = findViewById(R.id.liste_de_course);
        buttonAjoutListe = findViewById(R.id.button_recette);

        //recupération du bottomView
        bottomNavigationView = findViewById(R.id.bottom_menu);
        //definition des actions des bouttons
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.liste_de_course:
                                Intent liste = new Intent(RecetteActivity.this, ListeDeCourseActivity.class);
                                startActivity(liste);
                                break;
                            case R.id.produit:
                                Intent produit = new Intent(RecetteActivity.this, ProduitActivity.class);
                                startActivity(produit);
                                break;
                            case R.id.recette:
                                break;
                        }
                        return false;
                    }
                });

        buttonAjoutListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(RecetteActivity.this);
                AlertDialog alertDialog = new AlertDialog.Builder(RecetteActivity.this).create();
                alertDialog.setTitle("Nouvelle recette :");
                alertDialog.setView(input, 50, 0, 50, 0); // 10 spacing, left and right
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseLinker linker = new DatabaseLinker( RecetteActivity.this );
                        Dao<Recette, Integer> daoRecette = null;
                        try {
                            daoRecette = linker.getDao( Recette.class );
                        } catch (java.sql.SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        String name = input.getText().toString();
                        Recette newRecette = new Recette();
                        newRecette.setLibelle(name);

                        try {
                            daoRecette.create(newRecette);
                        } catch (java.sql.SQLException throwables) {
                            throwables.printStackTrace();
                        }


                        Intent monIntent = new Intent(RecetteActivity.this, RecetteActivity.class);
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
            Dao<Recette, Integer> daoRecette = databaseManager.getDao( Recette.class );
            List<Recette> listeRecette = daoRecette.queryForAll();

            for (Recette liste : listeRecette) {
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
                        editClient.setImageTintMode(PorterDuff.Mode.DARKEN);
                        Intent monIntent = new Intent(RecetteActivity.this, ContenueRecetteActivity.class);
                        monIntent.putExtra("idRecette", liste.getId()) ;
                        startActivity(monIntent);
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

                        AlertDialog alertDialog = new AlertDialog.Builder(RecetteActivity.this).create(); //Read Update
                        alertDialog.setTitle("Supression");
                        alertDialog.setMessage("Êtes vous sur de vouloir supprimer cette recette ?");

                        alertDialog.setButton("Supprimer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String nameListe = "";
                                List<Produit_recette> produit_recette = new ArrayList<>();
                                try {

                                    //supression des produit de la recette
                                    Dao<Produit_recette, Integer> daoProduit_recette = databaseManager.getDao(Produit_recette.class);
                                    produit_recette =
                                            daoProduit_recette.query(
                                                    daoProduit_recette.queryBuilder().where()
                                                            .eq("recette_id", liste.getId())
                                                            .prepare());
                                    for(Produit_recette produit_voulu : produit_recette){
                                        daoProduit_recette.delete(produit_voulu);
                                    }

                                    nameListe = liste.getLibelle();
                                    daoRecette.delete(liste);
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

}

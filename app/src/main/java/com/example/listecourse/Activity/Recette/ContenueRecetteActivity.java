package com.example.listecourse.Activity.Recette;

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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listecourse.Activity.MainActivity;
import com.example.listecourse.Entity.Produit;
import com.example.listecourse.Entity.Produit_recette;
import com.example.listecourse.Entity.Recette;
import com.example.listecourse.Tools.DatabaseLinker;
import com.example.listecourse.Entity.ListeCourse;
import com.example.listecourse.R;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedDelete;

import java.util.ArrayList;
import java.util.List;

public class ContenueRecetteActivity extends AppCompatActivity{
    private Dao<ListeCourse, Integer> daoListeCourse;
    private LinearLayout linearLayoutButton;
    private TableLayout containerListe;
    private Button buttonAjoutProduit;
    private Button buttonModificationProduit;
    private EditText nomRecette;
    private int idRecette;
    private Recette recetteChoisie;

    @SuppressLint({"RestrictedApi", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contenu_recette);

        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        idRecette = intent.getIntExtra("idRecette", 0);

        containerListe = findViewById(R.id.container_recette_produit);
        buttonAjoutProduit = findViewById(R.id.button_add_produit_to_recette);

        buttonAjoutProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent monIntent = new Intent(ContenueRecetteActivity.this, AjoutProduitActivity.class);
                monIntent.putExtra("idRecette", idRecette) ;
                startActivity(monIntent);
            }
        });

        DatabaseLinker databaseManager = new DatabaseLinker( this );
        try {
            Dao<Recette, Integer> daoRecette = databaseManager.getDao( Recette.class );
            recetteChoisie = daoRecette.queryForId(idRecette);
            this.getSupportActionBar().setTitle(recetteChoisie.getLibelle());
        } catch (java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        updateRecetteButton();
        createContenuRecette();
    }

    private void updateRecetteButton(){
        buttonModificationProduit = findViewById(R.id.button_validate);

        buttonModificationProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = new EditText(ContenueRecetteActivity.this);
                input.setText(recetteChoisie.getLibelle());
                AlertDialog alertDialog = new AlertDialog.Builder(ContenueRecetteActivity.this).create(); //Read Update
                alertDialog.setTitle("Modification");
                alertDialog.setMessage("Êtes vous sur de vouloir modifier le nom de cette recette ?");
                alertDialog.setView(input, 50, 0, 50, 0); // 10 spacing, left and right

                alertDialog.setButton("Modifier", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String nameListe = "";
                        DatabaseLinker databaseManager = new DatabaseLinker( ContenueRecetteActivity.this );
                        try {
                            Dao<Recette, Integer> daoRecette = databaseManager.getDao( Recette.class );
                            Recette recetteModifier = daoRecette.queryForId(idRecette);
                            if (recetteModifier != null) {
                                recetteModifier.setLibelle(input.getText().toString());
                                daoRecette.update(recetteModifier);
                            }
                        } catch (SQLException | java.sql.SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(), "nom de la recette modifier",Toast.LENGTH_LONG).show();

                        try {
                            Dao<Recette, Integer> daoRecette = databaseManager.getDao( Recette.class );
                            Recette recette = daoRecette.queryForId(idRecette);

                            ContenueRecetteActivity.this.getSupportActionBar().setTitle(recette.getLibelle());
                        } catch (java.sql.SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });

                alertDialog.show();  //<-- See This!
            }
        });
    }

    private void createContenuRecette() {
        containerListe.removeAllViews();

        DatabaseLinker databaseManager = new DatabaseLinker( this );
        try {
            List<Produit_recette> listProduit_recette = new ArrayList<>();
            Dao<Produit_recette, Integer> daoProduit_recette = databaseManager.getDao(Produit_recette.class);
            listProduit_recette =
                    daoProduit_recette.query(
                            daoProduit_recette.queryBuilder().where()
                                    .eq("recette_id", this.idRecette)
                                    .prepare());

            if(listProduit_recette.isEmpty()){
                TableRow row = new TableRow(this);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setWeightSum(8);

                TableRow.LayoutParams param = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        4f
                );

                TextView labelQuantite = new TextView(this);
                labelQuantite.setLayoutParams(param);
                labelQuantite.setText("Cette recette ne contient pas de produit");
                row.addView(labelQuantite);

                TableRow.LayoutParams paramButton = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f
                );

                containerListe.addView(row);
            }
            for (Produit_recette liste : listProduit_recette) {

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

                ImageButton editClient = new ImageButton(this);
                editClient.setLayoutParams(paramButton);
                editClient.setBackground(null);
                editClient.setImageResource(R.drawable.edit);
                row.addView(editClient);
                editClient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editClient.setImageTintMode(PorterDuff.Mode.DARKEN);
                        Intent monIntent = new Intent(ContenueRecetteActivity.this, AjoutProduitActivity.class);
                        monIntent.putExtra("idRecette", idRecette) ;
                        monIntent.putExtra("idProduitRecette", liste.getId()) ;
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
                        try {
                            daoProduit_recette.delete(liste);
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
        Intent retour = new Intent(ContenueRecetteActivity.this , RecetteActivity.class);
        startActivity(retour);
    }
}

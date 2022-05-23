package com.example.listecourse.Activity.Recette;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.listecourse.Activity.ListeDeCourse.AjoutProduitActivity;
import com.example.listecourse.Activity.ListeDeCourse.ContenueListeDeCourse;
import com.example.listecourse.Activity.ListeDeCourse.ListeDeCourseActivity;
import com.example.listecourse.Entity.ListeCourse;
import com.example.listecourse.Entity.Produit;
import com.example.listecourse.Entity.Produit_listeCourse;
import com.example.listecourse.Entity.Produit_recette;
import com.example.listecourse.Entity.Recette;
import com.example.listecourse.R;
import com.example.listecourse.Tools.DatabaseLinker;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProduitRecetteToListCours extends AppCompatActivity {

    private Dao<Recette, Integer> daoRecettes;
    private Dao<ListeCourse, Integer> daolisteCourse;
    private Dao<Produit_recette, Integer> daoProduitRecette;
    private Dao<Produit,Integer> daoProduit;
    private Dao<Produit_listeCourse,Integer> daoProduitlist;
    private List<Recette> listRecette;
    private List<Produit> produits;
    private LinearLayout viewRecette;
    private int idListe;
    private ListeCourse laliste;
    private List<Produit_listeCourse> listCours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produitrecettetolistcours);

        Intent intent = this.getIntent();
        idListe = intent.getIntExtra("idRecette", 0);

        viewRecette = findViewById(R.id.viewRecette);
        DatabaseLinker linker = new DatabaseLinker(ProduitRecetteToListCours.this);
        try {
            daoRecettes = linker.getDao( Recette.class );
            daolisteCourse = linker.getDao( ListeCourse.class );
            daoProduitRecette=linker.getDao(Produit_recette.class);
            daoProduitlist=linker.getDao(Produit_listeCourse.class);
            listRecette = daoRecettes.queryForAll();
            laliste = daolisteCourse.queryForId(idListe);
            listCours=daoProduitlist.queryForAll() ;
        }catch( Exception exception ) {}
        for (Recette recette: listRecette) {
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setWeightSum(8);

            viewRecette.addView(row);
            Button btnAdd = new Button(this);
            btnAdd.setText(recette.getLibelle());
            row.addView(btnAdd);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dao<Recette, Integer> daoRecette = null;
                    try {
                        daoRecette = linker.getDao( Recette.class );
                        daoProduit = linker.getDao(Produit.class);

                        List<Produit_recette> listProduitRecette = daoProduitRecette.queryForAll();
                        for(Produit_recette produit_recette : listProduitRecette){
                            if(produit_recette.getRecette()==recette){
                                for (Produit_listeCourse list : listCours){
                                    if (laliste=list.getListeCourse()){

                                    }
                                }
                            }
                        }
                    } catch (java.sql.SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    Intent monIntent = new Intent(ProduitRecetteToListCours.this, ListeDeCourseActivity.class);
                    startActivity(monIntent);
                }
            });



        }
    }
}

package br.com.etecia.projetolistacompras;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Compras_Activity extends AppCompatActivity {

    List<Item> itemList;
    ItemAdapter itemAdapter;
    SQLiteDatabase meuBancoDeDados;
    ListView listViewItens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compras_layout);

        listViewItens = findViewById(R.id.listarItensView);
        itemList = new ArrayList<>();

        meuBancoDeDados = openOrCreateDatabase(MainActivity.NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        visualizarItensDatabase();
    }

    //Irá listar todos os empregados do banco de dados
    private void visualizarItensDatabase() {

        Cursor cursorItem = meuBancoDeDados.rawQuery("SELECT * FROM compras", null);


        if (cursorItem.moveToFirst()) {
            do {
                itemList.add(new Item(
                        cursorItem.getInt(0), //id
                        cursorItem.getInt(1), //quantidade
                        cursorItem.getString(2),//nome do item
                        cursorItem.getString(3) //unidade
                ));
            } while (cursorItem.moveToNext());
        }
        cursorItem.close();

        //Verificar o layout
        itemAdapter = new ItemAdapter(this,R.layout.lista_itens,itemList,meuBancoDeDados);

        listViewItens.setAdapter(itemAdapter);
    }
}
package br.com.etecia.projetolistacompras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Variáveis globais
    public static final String NOME_BANCO_DE_DADOS = "itensLista.db";

    EditText txtItem, txtQuantidade;
    Button btnAdicionar, btnVisualizar;
    Spinner spnUnidades;

    SQLiteDatabase meuBancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //anunciando ao java componentes do xml
        txtItem = findViewById(R.id.txtItem);
        txtQuantidade = findViewById(R.id.txtQuantidade);

        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(this);

        btnVisualizar = findViewById(R.id.btnVisualizar);
        btnVisualizar.setOnClickListener(this);

        spnUnidades = findViewById(R.id.spnUnidades);

        //Criando banco de dados
        meuBancoDados = openOrCreateDatabase(NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        //criar a tabela no banco de dados especificado
        criarTabelaCompra();

    }

    // este método criará a tabela, mas como chamaremos esse método sempre que lançarmos o app,
    // adicionei IF NOT EXISTS ao SQL, ou seja, só criará a tabela quando a tabela ainda não estiver criada
    //-------------------------------------------------------------------------------------------------------------------
    private void criarTabelaCompra() {
        String sql =
                "CREATE TABLE IF NOT EXISTS compras (" +
                        "id integer PRIMARY KEY AUTOINCREMENT," +
                        "item varchar(200) NOT NULL," +
                        "quantidade varchar(200) NOT NULL," +
                        "unidade varchar(200) NOT NULL);";

        meuBancoDados.execSQL(sql);
    }
    //-------------------------------------------------------------------------------------------------------------------

    //Este método validará o nome do jogo e o tipo -------------------------------------------------------------------
    private boolean verificarEntrada(String item, String quantidade) {
        if (item.isEmpty()) {
            txtItem.setError("Insira um item por favor");
            txtItem.requestFocus();
            return false;
        }
        if (quantidade.isEmpty()) {
            txtQuantidade.setError("Insira uma quantidade por favor");
            txtQuantidade.requestFocus();
            return false;
        }
        return true;
    }
    //-------------------------------------------------------------------------------------------------------------------

    //Neste método vamos fazer a operação para adicionar os jogos
    private void adicionarItem() {

        String nomeItem = txtItem.getText().toString().trim();
        String quantidade = txtQuantidade.getText().toString().trim();
        String unidade =spnUnidades.getSelectedItem().toString();

        // obtendo o horário atual para data de inclusão
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dataEntrada = simpleDateFormat.format(calendar.getTime());

        //validando entrada
        if (verificarEntrada(nomeItem, quantidade)) {

            String insertSQL = "INSERT INTO compras (" +
                    "item, " +
                    "quantidade, " +
                    "unidade)" +
                    "VALUES(?, ?, ?);";

            // usando o mesmo método execSQL para inserir valores, desta vez tem dois parâmetros:
            // 1°string sql e 2° são os parâmetros que devem ser vinculados à consulta
            meuBancoDados.execSQL(insertSQL, new String[]{nomeItem, quantidade, unidade});

            Toast.makeText(getApplicationContext(), "Item adicionado com sucesso!", Toast.LENGTH_SHORT).show();

        }
    }
    //-------------------------------------------------------------------------------------------------------------------


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdicionar:
                adicionarItem();
                break;
            case R.id.btnVisualizar:
                startActivity(new Intent(getApplicationContext(), Compras_Activity.class));
                break;
        }

    }
}
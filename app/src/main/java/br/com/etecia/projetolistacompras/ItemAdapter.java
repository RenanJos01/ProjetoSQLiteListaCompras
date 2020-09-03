package br.com.etecia.projetolistacompras;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {


    //Variáveis globais

    Context mCtx;
    int listaLayoutRes;
    List<Item> listaItens;
    SQLiteDatabase meuBancoDeDados;


    //Construtor do adaptador

    public ItemAdapter(Context mCtx, int listaLayoutRes, List<Item> listaItens, SQLiteDatabase meuBancoDeDados) {
        super(mCtx, listaLayoutRes, listaItens);

        this.mCtx = mCtx;
        this.listaLayoutRes = listaLayoutRes;
        this.listaItens = listaItens;
        this.meuBancoDeDados = meuBancoDeDados;
    }


    //Inflar layout com o modelo e suas ações
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listaLayoutRes, null);

        final Item item = listaItens.get(position);

        TextView txtViewNome = view.findViewById(R.id.nomeItem);
        TextView txtViewQuantidade = view.findViewById(R.id.numQuantidade);
        TextView txtViewUnidade = view.findViewById(R.id.txtUnidade);

        txtViewNome.setText(item.getItem());
        txtViewQuantidade.setText(item.getQuantidade());
        txtViewUnidade.setText(item.getUnidade());

        Button btnExcluir = view.findViewById(R.id.btnExcluirViewItem);
        Button btnEditar = view.findViewById(R.id.btnEditarViewItem);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarItem(item);
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Deseja excluir?");
                builder.setIcon(android.R.drawable.ic_input_delete);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM compras WHERE id = ?";
                        meuBancoDeDados.execSQL(sql, new Integer[]{item.getId()});
                        //chamar o método para atualizar a lista de empregados
                        recarregarItensDB();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //não irá executar nada
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;


    }

    public void alterarItem(final Item item) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(R.layout.caixa_alterar_item, null);
        builder.setView(view);

        final EditText txtEditarItem = view.findViewById(R.id.txtEditarItem);
        final EditText txtEditarQuantidade = view.findViewById(R.id.txtEditarQuantidade);
        final Spinner spnUnidade = view.findViewById(R.id.spnUnidade);

        txtEditarItem.setText(item.getItem());
        txtEditarQuantidade.setText(item.getQuantidade());

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnAlterarItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = txtEditarItem.getText().toString().trim();
                String quantidade = txtEditarQuantidade.getText().toString().trim();
                String unidade = spnUnidade.getSelectedItem().toString().trim();

                if (nome.isEmpty()) {
                    txtEditarItem.setError("Nome está em branco");
                    txtEditarItem.requestFocus();
                    return;
                }
                if (quantidade.isEmpty()) {
                    txtEditarQuantidade.setError("Quantidade está em branco");
                    txtEditarQuantidade.requestFocus();
                    return;
                }

                String sql = "UPDATE compras SET nome = ?, unidade = ?, quantidade = ? WHERE id = ?";
                meuBancoDeDados.execSQL(sql,
                        new String[]{nome, unidade, quantidade, String.valueOf(item.getId())});
                Toast.makeText(mCtx, "Item alterado com sucesso!!!", Toast.LENGTH_LONG).show();

                //chamar o método para atualizar a lista de empregados
                recarregarItensDB();

                //limpa a estrutura do AlertDialog
                dialog.dismiss();
            }
        });

    }

    public void recarregarItensDB() {
        Cursor cursorItem = meuBancoDeDados.rawQuery("SELECT * FROM compras", null);
        if (cursorItem.moveToFirst()) {
            listaItens.clear();
            do {
                listaItens.add(new Item(
                        cursorItem.getInt(0),
                        cursorItem.getInt(1),
                        cursorItem.getString(2),
                        cursorItem.getString(3)
                ));
            } while (cursorItem.moveToNext());
        }
        cursorItem.close();
        notifyDataSetChanged();
    }
}

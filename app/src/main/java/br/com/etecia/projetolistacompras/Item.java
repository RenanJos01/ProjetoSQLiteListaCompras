package br.com.etecia.projetolistacompras;

public class Item {
    int id, quantidade;
    String item, unidade;

    public Item() {
    }

    public Item(int id, int quantidade, String item, String unidade) {
        this.id = id;
        this.quantidade = quantidade;
        this.item = item;
        this.unidade = unidade;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getQuantidade() {return quantidade;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}

    public String getItem() {return item;}
    public void setItem(String item) {this.item = item;}

    public String getUnidade() {return unidade;}
    public void setUnidade(String unidade) {this.unidade = unidade;}
}

public class Registro {
    Registro Proximo;

    private int chave;

    public Registro(int info){
        this.Proximo = null;
        this.chave = info;
    }

    public Registro getProximo() {
        return Proximo;
    }

    public void setProximo(Registro proximo) {
        Proximo = proximo;
    }

    public int getChave() {
        return chave;
    }

    public void setChave(int chave) {
        this.chave = chave;
    }
}
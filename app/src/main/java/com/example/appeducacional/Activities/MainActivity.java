
package com.example.appeducacional.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appeducacional.Classes.Usuarios;
import com.example.appeducacional.DAO.ConfiguracaoFireBase;
import com.example.appeducacional.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText TextoEmail,TextoSenha;
    private Button Entrar,Cadastrar;
    public static Usuarios usuario;
    public static FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        TextoEmail = (EditText) findViewById(R.id.EmailCadastroId);
        TextoSenha = (EditText) findViewById(R.id.SenhaCadastroId);
        Entrar = (Button) findViewById(R.id.EntrarId);
        Cadastrar = (Button)findViewById(R.id.CadastroId);

        //Verificação de usuario logado
       if(usuarioLogado()){
           //Se ja estiver logado vai para tela de menu
           if(true){
               //Verificação do tipo de usuário(adm,aluno,professor)
               if(usuario.getSenha().equals("adminmaster")){
                   Intent intentMinhaConta = new Intent(MainActivity.this, MenuAdminActivity.class);
                   AbrirNovaActivity(intentMinhaConta);
               }else if(usuario.getSenha().equals("proflucianabio")){
                   Intent intentMinhaConta = new Intent(MainActivity.this, MenuProfessorActivity.class);
                   AbrirNovaActivity(intentMinhaConta);
               }else{
                   Intent intentMinhaConta = new Intent(MainActivity.this, MenuAlunoActivity.class);
                   AbrirNovaActivity(intentMinhaConta);
               }
           //Caso não esteja logado ao clicar no botão entrar faz verificaçao de e-mail e senha
           }else{
               //Evento ao clicar no botao de entrar que loga o usuário
               Entrar.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       String EmailDigitado = TextoEmail.getText().toString();
                       String SenhaDigitada = TextoSenha.getText().toString();
                       //Verificação de campos digitados corretamente
                       if(!EmailDigitado.equals("") && !SenhaDigitada.equals("")){
                           //Salva as informação na classe usuário
                           usuario = new Usuarios();
                           usuario.setEmail(EmailDigitado);
                           usuario.setSenha(SenhaDigitada);
                           validarLogin(usuario.getEmail());

                       }else if(EmailDigitado.equals("") && SenhaDigitada.equals("")){
                           TextoEmail.setError("Erro digite seu e-mail");
                           TextoSenha.setError("Erro digite sua senha");

                       }else if(SenhaDigitada.equals("")){
                           TextoSenha.setError("Erro digite sua senha");

                       }else{
                           TextoEmail.setError("Erro digite seu e-mail");
                       }

                   }
               });
           }

       }


        //Botao que chama a tela de cadastro
        Cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCadastro = new Intent(MainActivity.this, CadastroActivity.class);
                AbrirNovaActivity(intentCadastro);
            }
        });


    }

    //Função que verifica se o usuário esta contido no Banco de dados e valida sua entrada
    private void validarLogin(final String email){
        autenticacao = ConfiguracaoFireBase.getFirebaseAuth();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    //Verificação do tipo de usuário(adm,aluno,professor)
                    if(usuario.getSenha().equals("adminmaster")){
                        Toast.makeText(MainActivity.this, email+" Logado com sucesso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MenuAdminActivity.class);
                        startActivity(intent);
                    }else if(usuario.getSenha().equals("proflucianabio")){
                        Toast.makeText(MainActivity.this, email+" Logado com sucesso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MenuProfessorActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, email+" Logado com sucesso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MenuAlunoActivity.class);
                        startActivity(intent);
                    }

                }else{
                    Toast.makeText(MainActivity.this, " Usuario ou senha invalidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    //Verifica se o usuário ainda está logado
    public  Boolean usuarioLogado(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            return true;
        }else{
            return false;
        }

    }

    //Função genérica para estartar uma nova activity
    public void AbrirNovaActivity(Intent intent){
        startActivity(intent);
    }


}

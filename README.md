# 1 - Criando um projeto básico com Spring-Batch.

Foi criado o projeto com as dependendências do spring-bathc e h2 com uma "pequena tarefa"(tasklet) como exemplo e execução básica.

Antes da criação do projeto foi instalado e configurado o mysql com um usuário para utilização. Segue o procedimento realizado:


Instalação do mysql no host local:

sudo apt install mysql-server

acessar o console do sgbd mysql-server:

sudo mysql

Para utilização de senhas simples no mysql, é necessário desinstalar o plugin "validate_password"(Não recomendado. Apenas para fins de laboratório).

uninstall plugin validate_password; (Não funcionou).

Criando o usuário do projeeto

CREATE USER 'batch'@'localhost' IDENTIFIED BY '123456';

Dando todos os privilégios ao usuário novo:

GRANT ALL PRIVILEGES ON *.* TO 'batch'@'localhost';

Logando com o novo usuário:

mysql -u batch -p

Criando o novo banco do projeto:


# 2 - Implementando e entendendo Jobs e Steps

Obs. Considerando que o h2 "perde" os metadados ao finalizar a aplicação, a partir de agora será utilizado o mysql(Incluindo a dependência no pom.xml)
para persistir esses metadados, tomando como um pré requisito.

Incluídas as dependências do H2 e conector do mysql. Incluídos os profiles dos properties de conexão do BD.

# 3 - Múltiplas Execuções e Escopo
- Possibilitando o Job lógico(Job Instance) ser executado todas as vezes através do incremento de execução(Job execution). Assim,
não ocorrerá mais a mensagem no log informando que o Job já foi executado.

Obs: Cuidado com este recurso, pois este incremento impede o outro importante recurso que é a reinicialização do Job.
Caso o processamento seja interrompido, retomar de onde parou não seria possíve, pois seria criada um nova instância do Job, 
devido ao incrementador estar configurado. Assim, não seria possível retomar o processamento a partir da última instância interrompida.
(Removeremos mais a frente)

É possível capturar os parâmetros passados na execução do job e impri-los no log de execução.

# 4 - Tipos de Steps
Um Job é definido por uma sequência encadeada de steps e cada step tem sua própria lógica e implementação.

As 2 formas de execução de uma tarefa(Task) da Step são:
* a) Tasklet - Tarefas simples/pequenas usadas geralmente como pré-processamento com um único comando para executar
    (ex: criação de diretórios, limpeza de arquivos, etc).
* b) Chunck - Tarefas e processamentos complexos que precisam ser executadas em pedaços(Chunck), sendo que cada Chunck são divididos em:
  * Leitura(ItemReader)
  * Processamento(ItemProcessor). Obs: Importante destacar que cada Chunck possui sua própria transação. Assim, qualquer interrupção não afetará os chuncks anteriores
  * Escrita(ItemWriter)

5 - Criando Step baseada em Chunck
A implementação tem algumas observações importantes.
- 1 - Quando a aplicação possui apenas um Job no contexto do Spring, ele é executado por padrão. No entando, quando múltiplos job estão presentes no contexto do Spring, é necessário informar ao Spring
qual job executar, isto quando os jobs executam no startup da aplicação.(Desabilitado o outro job(Comentando a anotation @Configuration))

Obs: A outra opção é desabilitar a execução no startup (spring.batch.job.name=multipleStepJob) e implementar programaticamente usando JobLouncher.
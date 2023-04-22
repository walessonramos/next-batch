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


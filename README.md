# Danillo Wesley da Costa Silva

## 👨‍🎓 Introdução

Desenvolvedor de Software e aluno do Curso Superior de Tecnologia em Banco de Dados na Fatec (São José dos Campos/SP).

## 📬 Contatos

* [GitHub](https://www.github.com/xxzidanilloxx)
* [LinkedIn](https://www.linkedin.com/in/danillowesley)

## 🧠 Meus Principais Conhecimentos

- Java
- Spring
- SQL

## 🚀 Meus Projetos

<details>
<summary><b> 2022-2 </b></summary>

Este projeto foi desenvolvido em parceria com a **Fatec**, que apresentou como demanda a criação de um sistema para Avaliação Técnica 360°, no qual os usuários realizam autoavaliação e também avaliam os demais membros da equipe. A solução desenvolvida conta com dois perfis de usuários: o administrador, responsável pela gestão e cadastro de usuários básicos, garantindo controle e organização; e o usuário básico, que atribui notas aos avaliados e acessa um dashboard interativo para monitorar métricas e resultados.
O sistema foi projetado para oferecer uma interface intuitiva e funcional, com foco em eficiência e facilidade de uso, atendendo às demandas de avaliação em diferentes contextos organizacionais.

[GitHub](https://github.com/tn-api1sem/api)

#### Tecnologias Utilizadas

- **Python**: Linguagem de programação versátil e poderosa, utilizada para implementar a lógica central e funcionalidades robustas da aplicação.
- **FastAPI**: Framework de alto desempenho para Python, escolhido para construir APIs rápidas, seguras e bem documentadas, com suporte a validações e autenticação de maneira eficiente.
- **JavaScript**: Responsável por adicionar interatividade e dinamismo à aplicação, tornando a interface mais responsiva e enriquecendo a experiência do usuário.
- **HTML**: Linguagem de marcação utilizada para estruturar e organizar os elementos da interface, garantindo uma base semântica sólida para a aplicação.
- **CSS**: Linguagem de estilo utilizada para definir a aparência e o layout da aplicação. Foi essencial para garantir uma experiência visual agradável e alinhada aos objetivos do projeto. 

#### Contribuições Pessoais

<details>
<summary><b> Implementação do repositório para os times </b></summary>

O repositório gerencia as operações de CRUD (Create, Read, Update, Delete) relacionadas à tabela de times e é responsável pelo acesso direto ao banco de dados.

```python

class times_repository(object):
    _apiContext: ApiContext = ApiContext()

    def __init__(self) -> None:
        pass

    def get(self):
        return self._apiContext.times_table.get_all()

    def busca_id_times(self, id):
        return self._apiContext.times_table.get(id)

    def findTeamByGroup(self, idGroup:int):
        times = self.get();

        timesNoGrupo = [];
        for time in times:
            if time.id_group == idGroup:
                timesNoGrupo.append(time)
        return timesNoGrupo

    def post_times(self, objectPost):
        self._apiContext.times_table.begin_transaction()
        self._apiContext.times_table.insert(objectPost)
        self._apiContext.times_table.commit()

    def update(self, objectPut):
        self._apiContext.times_table.begin_transaction()
        self._apiContext.times_table.update(objectPut)
        self._apiContext.times_table.commit()

    def delete_id_times(self, id: int):
        self._apiContext.times_table.begin_transaction()
        self._apiContext.times_table.delete(id)
        self._apiContext.times_table.commit()
```
</details>

<details>
<summary><b> Implementação do serviço para os times </b></summary>

A camada de serviço implementa a lógica de negócios, utilizando os métodos fornecidos pelo repositório. Além das operações básicas, inclui funcionalidades como mapeamento e montagem de objetos de resposta personalizados, gestão de associações entre times e usuários, como criação, atualização e exclusão de relacionamentos, além de uma busca detalhada por times com informações completas sobre seus integrantes e perfis.

```python
class times_services(object):
    _times_repository: times_repository = times_repository()
    _usuario_repository: user_repository = user_repository()
    _userteam_repository: userteam_repository = userteam_repository()
    _profiles_repository: profille_repository = profille_repository()

    def __init__(self):
        pass

    def buscar_times(self):
        times = self._times_repository.get()
        profiles = self._profiles_repository.get();
        
        timeResponseList = []

        for time in times:
            timeResponse = self.mapToTimeResponse(time)
            userTeams = self._userteam_repository.get_user_teams_by_team_id(
                time.id)
            for userTeam in userTeams:
                user = self._usuario_repository.get_by_id(userTeam.id_user);
                timeResponse.userName.append(user.usuario+' ['+profiles[userTeam.id_profile-1].perfil+']')
                timeResponse.id_users.append(user.id)

            timeResponseList.append(timeResponse)

        return timeResponseList

    def mapToTimeResponse(self, time):
        timeResponse = times_get_response()
        timeResponse.id = time.id
        timeResponse.id_group = time.id_group
        timeResponse.times = time.times
        timeResponse.userName = []
        timeResponse.id_users = []
        return timeResponse

    def buscar_id_times(self, id):
        time = self._times_repository.busca_id_times(id)
        if (not time):
            return None
        else:
            user_teams = self._userteam_repository.get_user_teams_by_team_id(
                time.id)

        model = times_model()
        model.id = time.id
        model.times = time.times
        model.times_model = []

        for u_t in user_teams:
            u_t.user = self._usuario_repository.get_by_id(u_t.id_user).usuario
            u_t.profile = self._profiles_repository.find(u_t.id_profile).perfil

            model.times_model.append(u_t)

        return model

    def post_times(self, model: times_model):
        modelToInsert = times_bd()
        modelToInsert.id = model.id
        modelToInsert.times = model.times
        self._times_repository.post_times(modelToInsert)

        self.create_user_team(modelToInsert.id, model.times_model)

    def put_times(self, model: times_model):
        modelToInsert = times_bd()
        modelToInsert.id = model.id
        modelToInsert.times = model.times

        self._times_repository.update(modelToInsert)
        self.update_user_team(modelToInsert.id, model.times_model)

    def delete(self, id: int):
        self.delete_user_team(id)
        return self ._times_repository.delete_id_times(id)

    def create_user_team(self, idGroup: int, userTeams: list[user_team_model]):
        for userTeam in userTeams:
            userTeam.id_team = idGroup
            self._userteam_repository.create(userTeam)

    def update_user_team(self, idTeam: int, userTeams: list[user_team_model]):
        self.delete_user_team(idTeam)
        for userTeam in userTeams:
            self._userteam_repository.create(userTeam)

    def delete_user_team(self, idTeam: int):
        allUserProfiles = self._userteam_repository.get_user_teams_by_team_id(
            idTeam)

        for userProfile in allUserProfiles:
            self._userteam_repository.delete(userProfile.id)
```
</details>

<details>
<summary><b> Implementação do controlador para os times </b></summary>

O controlador expõe as funcionalidades através de endpoints RESTful, implementados com FastAPI. Ele define as rotas da API para atender as operações de recuperar todos os times, criar novos registros de times, atualizar informações de times existentes ou remover times.

```python
router = APIRouter(
    prefix="/api/v1/times",
    tags=["times"],
    responses={404: {"description": "Not found"}},
)
times_services = TimesService()

@router.get("/")
def get_times():
    try:
        return times_services.buscar_times()
    except Exception as e:
        return str(e)


@router.get("/{id}")
def get_time_by_id(id: int):
    return times_services.buscar_id_times(id)


@router.post("/")
def post_times(objectToPost: times_model):
    try:
        times_services.post_times(objectToPost)
        return OK
    except Exception as e:
        return str(e)

@router.put("/")
def put_times(objectToPut: times_model):
    try:
        times_services.put_times(objectToPut)
        return OK
    except Exception as e:
        return str(e)

@router.delete("/{id}")
def delete_id_times(id:int):
    times_services.delete(id)
    return OK
```
</details>

### Hard Skills
- **Desenvolvimento Back-End:** Desenvolvi APIs RESTful utilizando FastAPI, com ênfase em desempenho, validação de dados e uma arquitetura modular, estruturando de forma clara os serviços, repositórios e controladores.
- **Programação Orientada a Objetos (POO):** Apliquei os princípios da Programação Orientada a Objetos em Python, desenvolvendo classes com responsabilidades bem definidas, o que contribuiu para a organização e manutenibilidade do código.

### Soft Skills
- **Adaptação e Aprendizado Contínuo:** Demonstrei facilidade em aprender novas tecnologias de forma autônoma e em aplicá-las de maneira eficaz para otimizar processos e resultados.
- **Resolução de Problemas:** Atuei na análise e resolução de problemas, identificando causas e propondo soluções práticas e eficientes, sempre alinhadas aos objetivos do projeto.
- **Trabalho em Equipe:** Participei ativamente da colaboração entre membros da equipe de desenvolvimento, contribuindo com ideias, alinhamento técnico e integração de funcionalidades de forma coesa.

</details>

<details>
<summary><b> 2023-1 </b></summary>

A empresa parceira **2RP** propôs o desenvolvimento de um sistema desktop de controle de horas extras, com a necessidade de registrar, visualizar em tempo real e extrair relatórios detalhados das horas trabalhadas. A solução implementada contempla o CRUD de usuários, centros de resultado e clientes, além da parametrização do sistema quanto aos valores das taxas de trabalho e horários de jornada noturna. O sistema permite que administradores aprovem ou reprovem horas extras, gestores lancem horas e acompanhem relatórios, e usuários registrem e acompanhem suas horas mensais acumuladas.

[GitHub](https://github.com/api-2-sem)

#### Tecnologias Utilizadas

- **Java**: Linguagem de programação amplamente utilizada para desenvolvimento de aplicativos devido à sua portabilidade, robustez e segurança. Foi fundamental fornecendo uma base sólida e confiável para o projeto.
- **JavaFX**: Biblioteca utilizada para o desenvolvimento de interfaces gráficas (GUIs) para aplicações desktop, permitindo a criação de componentes visuais interativos e atraentes.
- **CSS**: Linguagem de estilo utilizada para definir a aparência e o layout da aplicação. Foi essencial para garantir uma experiência visual agradável e alinhada aos objetivos do projeto.
- **MySQL**: Sistema de gerenciamento de banco de dados relacional que oferece desempenho, confiabilidade e escalabilidade, utilizado para armazenar e gerenciar os dados da aplicação de forma eficiente e segura.

#### Contribuições Pessoais

<details>
<summary><b> Implementação de controle e gerenciamento de usuários </b></summary>

A atividade consiste em criar uma interface gráfica com JavaFX para gerenciar usuários, integrando funcionalidades como visualização, exclusão e filtro de dados. A aplicação exibe informações dos usuários em uma tabela, com opções para excluir registros diretamente e filtrar por seleção em um combo box. Além disso, permite navegação para a tela de cadastro de novos usuários e retorno ao menu principal.

```java
public class VisualizarUsuarioController {
	private UsuarioDAO usuarioDAO;

	public VisualizarUsuarioController() {
		Connection connection = new ConnectionFactory().recuperarConexao();
		this.usuarioDAO = new UsuarioDAO(connection);
	}
	
	@FXML ComboBox comboUsuario;
	@FXML private TableView<UsuarioDTO> tabelaUsuarios;
	@FXML private TableColumn<UsuarioDTO, String> colNome;
	@FXML private TableColumn<UsuarioDTO, String> colCPFCNPJ;
	@FXML private TableColumn<UsuarioDTO, String> colEmail;
	@FXML private TableColumn<UsuarioDTO, TipoUsuario> colTipo;
	@FXML private TableColumn<UsuarioDTO, Void> colAcoes;
		
	private ObservableList<UsuarioDTO> usuario = FXCollections.observableArrayList();
	
	@FXML
	protected void initialize() {
		carregarCombobox();
		colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		colCPFCNPJ.setCellValueFactory(new PropertyValueFactory<>("cpf_cnpj"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colTipo.setCellValueFactory(new PropertyValueFactory<>("idTipoUsuario"));
		colAcoes.setCellValueFactory(new PropertyValueFactory<>(""));
		buscarUsuario(null);
	}
	
	public void carregarCombobox() {
		usuario.addAll(usuarioDAO.getNomeUsuarioAndId());
		comboUsuario.setItems(usuario);
	}
	
	public void buscarUsuario(ActionEvent event) {
		adicionarBotaoDeletar();
		UsuarioDTO usuario = (UsuarioDTO) comboUsuario.getSelectionModel().getSelectedItem();
		List<UsuarioDTO> usuarios = null;

		try {
			usuarios = UsuarioDAO.listarUsuarios(usuario != null ? usuario.getId() : null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tabelaUsuarios.setItems(listarUsuario(usuarios));

	}
		
	public void irCadastroUsuario(ActionEvent event) {
		ChangeScene cs = new ChangeScene();
		cs.irCadastroUsuario();
	}
	
	private ObservableList<UsuarioDTO> listarUsuario(List<UsuarioDTO> usuarios){
		return FXCollections.observableArrayList(usuarios);
	}
	
    @FXML
    void retornarMenu(MouseEvent event) {
        MenuController.irMenu();
    }
    
    public void adicionarBotaoDeletar() {
        var buttonDeletar = new Callback<TableColumn<UsuarioDTO, Void>, TableCell<UsuarioDTO, Void>>() {
            @Override
            public TableCell<UsuarioDTO, Void> call(final TableColumn<UsuarioDTO, Void> param) {
                final TableCell<UsuarioDTO, Void> cell = new TableCell<UsuarioDTO, Void>() {

                    private final Button btn = new Button("Excluir");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            var row = getTableView().getItems().get(getIndex());
                            getTableView().getItems().remove(getIndex());
                            usuarioDAO.deletar(row.getId());          
                            return;
                         });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colAcoes.setCellFactory(buttonDeletar);
    }
}
```
</details>

<details>
<summary><b> Arquivo FXML para interface de controle de usuários </b></summary>

Este arquivo FXML define a interface gráfica utilizada na aplicação de controle de usuários. O layout é organizado em um AnchorPane, com uma barra lateral para navegação, um título principal e componentes interativos. A tabela exibe dados como nome, CPF/CNPJ, email, tipo de usuário e oferece ações específicas. O combo box é usado para filtrar a tabela, enquanto botões adicionais permitem buscar registros, criar novos usuários e retornar ao menu. O design utiliza classes de estilo e imagens para aprimorar a aparência e a experiência do usuário.

```fxml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.Button?>
<?import javafx.scene.control.ComboBox?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.TableColumn?>
<?import javafx.scene.control.TableView?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.Pane?>
<?import javafx.scene.text.Font?>

<AnchorPane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="609.0" prefWidth="944.0" style="-fx-background-color: fffff;" xmlns="http://javafx.com/javafx/19" xmlns:fx="http://javafx.com/fxml/1" fx:controller="controller.VisualizarUsuarioController">
   <children>
      <Pane prefHeight="610.0" prefWidth="113.0" styleClass="sidebar">
         <children>
            <ImageView fitHeight="52.0" fitWidth="93.0" layoutX="15.0" layoutY="36.0" pickOnBounds="true" preserveRatio="true">
               <image>
                  <Image url="@../figma/2rp.png" />
               </image>
            </ImageView>
            <Button layoutX="34.0" layoutY="538.0" mnemonicParsing="false" scaleX="0.5" scaleY="0.5" text="Button" />
            <ImageView fitHeight="40.0" fitWidth="43.0" layoutX="42.0" layoutY="533.0" onMouseClicked="#retornarMenu" pickOnBounds="true" preserveRatio="true">
               <image>
                  <Image url="@../figma/log.png" />
               </image>
            </ImageView>
         </children></Pane>
      <Label layoutX="395.0" layoutY="35.0" text="Controle de Usuarios" textFill="#483fee">
         <font>
            <Font size="27.0" />
         </font>
      </Label>
      <Button layoutX="816.0" layoutY="118.0" minHeight="30.0" minWidth="135.0" mnemonicParsing="false" onAction="#buscarUsuario" prefHeight="30.0" prefWidth="100.0" styleClass="MYButton" text="Buscar">
         <font>
            <Font size="14.0" />
         </font></Button>
      <TableView fx:id="tabelaUsuarios" layoutX="158.0" layoutY="171.0" prefHeight="344.0" prefWidth="758.0">
        <columns>
          <TableColumn fx:id="colNome" prefWidth="247.0" text="Nome" />
          <TableColumn fx:id="colCPFCNPJ" prefWidth="238.0" text="CPF/CNPJ" />
          <TableColumn fx:id="colEmail" prefWidth="247.0" text="Email" />
          <TableColumn fx:id="colTipo" prefWidth="247.0" text="Tipo do Usuario" />
          <TableColumn fx:id="colAcoes" prefWidth="247.0" text="Ações" />
        </columns>
         <columnResizePolicy>
            <TableView fx:constant="CONSTRAINED_RESIZE_POLICY" />
         </columnResizePolicy>
      </TableView>
      <ComboBox fx:id="comboUsuario" layoutX="157.0" layoutY="120.0" prefHeight="30.0" prefWidth="589.0" promptText="Usuário" styleClass="input-text" />
      <Button layoutX="157.0" layoutY="534.0" minHeight="30.0" minWidth="135.0" mnemonicParsing="false" onAction="#irCadastroUsuario" prefHeight="30.0" prefWidth="135.0" styleClass="MYButton" text="Criar Usuario">
         <font>
            <Font size="14.0" />
         </font>
      </Button>
   </children>
</AnchorPane>
```
</details>

#### Hard Skills

- **Programação em Java:** Desenvolvi funcionalidades utilizando JavaFX, integrando a lógica da interface gráfica com o controle e persistência de dados em banco de dados.
- **Controle de Fluxo de Dados:** Implementei mecanismos de navegação e filtragem de registros, assegurando maior eficiência e usabilidade na manipulação dos dados.
- **JavaFX:** Projetei e implementei interfaces gráficas interativas, utilizando elementos como tabelas, combo boxes e botões para o gerenciamento intuitivo de informações dos usuários.

#### Soft Skills

- **Trabalho em equipe:** Colaborei ativamente com colegas de equipe na integração entre a interface gráfica e a lógica de persistência, promovendo alinhamento técnico e coesão no desenvolvimento do sistema.
- **Gestão de tempo:** Organizei tarefas e cronogramas de forma a cumprir os prazos estabelecidos, garantindo a entrega eficiente das funcionalidades propostas.
- **Resolução de problemas:** Atuei na identificação e correção de falhas funcionais, como erros na exclusão ou filtragem de dados, propondo soluções ágeis e eficazes para manter a estabilidade da aplicação.

</details>

<details>
<summary><b> 2023-2 </b></summary>

A empresa parceira **2RP** propôs o desenvolvimento de um sistema web de controle de horas extras, com a necessidade de registrar, visualizar em tempo real e extrair relatórios detalhados das horas trabalhadas. A solução implementada contempla o CRUD de usuários, centros de resultado e clientes, além da parametrização do sistema quanto aos valores das taxas de trabalho e horários de jornada noturna. O sistema permite que administradores aprovem ou reprovem horas extras, gestores lancem horas e acompanhem relatórios, e usuários registrem e acompanhem suas horas mensais acumuladas.

[GitHub](https://github.com/api-3-sem)

#### Tecnologias Utilizadas

- **Java**: linguagem de programação amplamente utilizada para desenvolvimento de aplicativos devido à sua portabilidade, robustez e segurança. Foi fundamental fornecendo uma base sólida e confiável para o projeto.
- **Spring**: framework para desenvolvimento de aplicativos Java que oferece um conjunto abrangente de ferramentas e bibliotecas para simplificar o desenvolvimento. Ele fornece recursos como injeção de dependência, controle transacional e segurança, ajudando na criação de uma aplicação robusta e de fácil manutenção.
- **VueJS**: framework para construção de interfaces de usuário. Ele é conhecido por sua simplicidade e flexibilidade, permitindo o desenvolvimento ágil de interfaces interativas e responsivas, criando uma experiência de usuário moderna e intuitiva no frontend da aplicação.
- **MySQL**: sistema de gerenciamento de banco de dados relacional que oferece desempenho, confiabilidade e escalabilidade, utilizado para armazenar e gerenciar os dados da aplicação de forma eficiente e segura.

#### Contribuições Pessoais

Durante o projeto atuei no back-end da aplicação, contribuindo na implementação do dashboard.

<details>
<summary><b> Implementação do Data Transfer Object (DTO) responsável pelos dados de entrada e retorno para a consulta do dashboard: </b></summary>

O record DadosDashboard representa os dados de entrada para a consulta do dashboard. Ele possui quatro componentes: idCliente, idCr, dataInicio e dataFim.
```java
public record DadosDashboard(Long idCliente, Long idCr, Date dataInicio, Date dataFim) {

}
```

O record DadosRetornoDashboard representa os dados de retorno da consulta do dashboard. Ele possui cinco componentes: horas, razaoSocial, nome, idUsuario e modalidade.
```java
public record DadosRetornoDashboard(double horas, String razaoSocial, String nome, int idUsuario, String Modalidade) {

}
```
</details>

<details>
<summary><b> Implementação do repository: </b></summary>

Esta interface define um contrato para operações de consulta personalizadas relacionadas aos lançamentos de horas.
Possui um método findHoras que espera receber um objeto do tipo DadosDashboard como entrada e retorna uma lista de objetos DadosRetornoDashboard.
```java
public interface CustomLancamentoHorasRepository {
	...
	List<DadosRetornoDashboard> findHoras(DadosDashboard dados);
}
```

Esta classe implementa a interface CustomLancamentoHorasRepository e fornece a implementação concreta do método findHoras.
O método findHoras constrói uma consulta JPQL (Java Persistence Query Language) para recuperar os dados do dashboard com base nos parâmetros fornecidos no objeto DadosDashboard.
A consulta calcula a soma das horas entre as colunas DataHora_Inicio e DataHora_Fim da tabela extrato_hora, agrupando os resultados por diferentes critérios.
Os parâmetros da consulta são passados dinamicamente através do objeto DadosDashboard.
```java
public class CustomLancamentoHorasRepositoryImpl implements CustomLancamentoHorasRepository {
	...
	public List<DadosRetornoDashboard> findHoras(DadosDashboard dados){
		String jpql = "SELECT SUM(TIMESTAMPDIFF(HOUR,a.DataHora_Inicio, a.DataHora_Fim)) Horas,"
				+ "    b.Razao_Social,"
				+ "    c.Nome,"
				+ "    a.Id_Usuario,"
				+ "    a.Modalidade"
				+ "	   FROM ("
				+ "	   SELECT"
4				+ "		Id_Cliente,"
				+ "		Id_Cr,"
				+ "		Id_Modalidade,"
				+ "		Id_Usuario,"
				+ "		CASE"
				+ "			WHEN DATE_FORMAT(DataHora_Inicio, '%H:%i:s') >= :horarioNoturno AND DATE_FORMAT(DataHora_Fim , '%H:%i:s') <= :horarioMatutino and Id_Modalidade = 1 then 1"
				+ "			WHEN DATE_FORMAT(DataHora_Inicio, '%H:%i:s') < :horarioNoturno AND DATE_FORMAT(DataHora_Fim , '%H:%i:s') > :horarioMatutino and Id_Modalidade = 1 then 2"
				+ "			WHEN DATE_FORMAT(DataHora_Inicio, '%H:%i:s') >= :horarioNoturno AND DATE_FORMAT(DataHora_Fim , '%H:%i:s') <= :horarioMatutino and Id_Modalidade = 2 then 3"
				+ "			WHEN DATE_FORMAT(DataHora_Inicio, '%H:%i:s') < :horarioNoturno AND DATE_FORMAT(DataHora_Fim , '%H:%i:s') > :horarioMatutino and Id_Modalidade = 2 then 4"
				+ "		END Modalidade,"
				+ "		DataHora_Inicio,"
				+ "		DataHora_Fim"
				+ "		FROM extrato_hora"
				+ "	WHERE id_cliente = :idCliente"
				+ "	AND id_cr = :idCr"
				+ " 	AND dataHora_inicio >= :dataInicio"
				+ " 	AND dataHora_fim <= :dataFim"
				+ " 	) AS a"
				+ " 	JOIN cliente b ON a.Id_cliente = b.id"
				+ " 	JOIN cr c ON c.Id = a.id_cr"
				+ " 	JOIN modalidade d ON d.Id = a.id_Modalidade"
				+ " 	GROUP BY a.Modalidade, b.Razao_Social, c.Nome, d.Descricao, Id_Usuario";
	}
}
```
</details>

<details>
<summary><b> Implementação do service: </b></summary>

Esta classe encapsula a lógica de negócios relacionada ao dashboard. Possui um método findDashboard que recebe um objeto DadosDashboard, chama o método findHoras do repositório e retorna os dados do dashboard.
```java
@Service
public class DashboardService {

	@Autowired
	LancamentoHorasRepository repository;

	public List<DadosRetornoDashboard> findDashboard(DadosDashboard dados){
		 List<DadosRetornoDashboard> dadosRetorno = new ArrayList<DadosRetornoDashboard>();
		 return dadosRetorno = repository.findHoras(dados);
	}
}
```
</details>

<details>
<summary><b> Implementação do controller: </b></summary>
	
Este é um controlador Spring MVC que lida com requisições relacionadas ao dashboard. Possui um endpoint HTTP GET /dashboard que espera parâmetros opcionais (idCliente, idCr, dataInicio e dataFim).
Quando uma solicitação é recebida neste endpoint, os parâmetros são encapsulados em um objeto DadosDashboard e passados para o serviço DashboardService.
O serviço executa a lógica de negócios e retorna os dados do dashboard, que são então serializados em JSON e enviados de volta como resposta HTTP.
```java
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	DashboardService service;

	@GetMapping
	public ResponseEntity<List<DadosRetornoDashboard>> findDashboard(@RequestParam(value="idCliente" , required = false) Long idCliente, @RequestParam(value="idCr", required = false) Long idCr, @RequestParam(value="dataInicio" , required = false) Date dataInicio,@RequestParam(value="dataFim", required = false) Date dataFim) throws Exception{
		DadosDashboard dados = new DadosDashboard(idCliente, idCr, dataInicio, dataFim, null, null);
		var dadosRetorno = service.findDashboard(dados);
		return ResponseEntity.ok().body(dadosRetorno);	
	}
}
```
</details>

#### Hard Skills

- **Java:** Desenvolvi a lógica de back-end utilizando Java, aplicando frameworks como Spring para criação de APIs e controle eficiente do fluxo de dados.
- **Spring Framework:** Projetei e implementei aplicações robustas com Spring Boot, explorando recursos como Injeção de Dependência, Segurança e Gerenciamento de Transações.
- **MySQL:** Modelei e mantive bancos de dados relacionais, realizando consultas SQL complexas para garantir a integridade e eficiência no acesso aos dados.
- **API RESTful:** Implementei endpoints seguindo arquitetura REST, proporcionando consumo estruturado e seguro dos dados pela aplicação cliente.

#### Soft Skills

- **Trabalho em equipe:** Colaborei de forma integrada com desenvolvedores e demais membros do projeto, garantindo a harmonização dos diferentes componentes da aplicação.
- **Gestão de tempo:** Organizei e priorizei tarefas para cumprir prazos estabelecidos, mesmo diante da execução simultânea de múltiplas atividades.
- **Resolução de problemas:** Analisei desafios técnicos com foco crítico e implementei soluções práticas que asseguraram a continuidade e qualidade do sistema.

</details>

<details>
<summary><b> 2024-1 </b></summary>

A empresa parceira **Oracle** identificou a necessidade de um sistema para acompanhamento do progresso de seus parceiros no programa de capacitação. O sistema desenvolvido permite o gerenciamento de usuários, empresas parceiras e trilhas de aprendizado, com acompanhamento em tempo real da taxa de conclusão dos cursos, distribuição geográfica dos parceiros e prazos de expiração das formações, além da emissão automatizada de notificações.

[GitHub](https://github.com/api-4-sem)

#### Tecnologias Utilizadas

- **Java**: Linguagem de programação amplamente utilizada para desenvolvimento de aplicativos devido à sua portabilidade, robustez e segurança. Foi fundamental fornecendo uma base sólida e confiável para o projeto.
- **Spring**: Framework para desenvolvimento de aplicativos Java que oferece um conjunto abrangente de ferramentas e bibliotecas para simplificar o desenvolvimento. Ele fornece recursos como injeção de dependência, controle transacional e segurança, ajudando na criação de uma aplicação robusta e de fácil manutenção.
- **VueJS**: Framework para construção de interfaces de usuário. Ele é conhecido por sua simplicidade e flexibilidade, permitindo o desenvolvimento ágil de interfaces interativas e responsivas, criando uma experiência de usuário moderna e intuitiva no frontend da aplicação.
- **Oracle Cloud**: Plataforma de nuvem que oferece uma ampla gama de serviços, como armazenamento, banco de dados e ferramentas de inteligência artificial. Proporcionou escalabilidade, alta disponibilidade e segurança, sendo crucial para hospedar e gerenciar a infraestrutura do projeto com eficiência.

#### Contribuições Pessoais

<details>
<summary><b> Implementação do DTO para Relatório </b></summary>

O objetivo da atividade foi implementar o Data Transfer Object (DTO) que representa os dados utilizados no relatório, como nome do colaborador, nome da trilha e nome da expertise, além do status.

```java
@Getter
@Setter
public class DadosRelatorio {
    private String nomeColaborador;
    private String nomeTrilha;
    private String nomeExpertise;
    private Status status;
}
```
</details>

<details>
<summary><b> Implementação do Serviço de Relatório </b></summary>

Nesta atividade, fui responsável por implementar o serviço que converte uma lista de DadosRelatorio em um arquivo Excel. Utilizei o Apache POI para gerar as planilhas e exportar os dados de forma eficiente.

```java
@Service
public class RelatorioService {
    
    public static String[] HEADERS={
            "Nome",
            "Trilha",
            "Expertise",
            "Status"
    };
    
    public static String SHEET_NAME="data";
    public static InputStreamResource dataToExcel(List<DadosRelatorio> dadosRelatorios) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            Sheet sheet = workbook.createSheet(SHEET_NAME);
            Row row = sheet.createRow(0);
            for (int i = 0;  i < HEADERS.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(HEADERS[i]);
            }
            int rowIndex = 1;
                        for (DadosRelatorio dadosRelatorio : dadosRelatorios) {
                Row dataRow = sheet.createRow(rowIndex);
                rowIndex++;
                dataRow.createCell(0).setCellValue(dadosRelatorio.getNomeColaborador());
                dataRow.createCell(1).setCellValue(dadosRelatorio.getNomeTrilha());
                dataRow.createCell(2).setCellValue(dadosRelatorio.getNomeExpertise());
                dataRow.createCell(3).setCellValue(dadosRelatorio.getStatus().name());
            }
            workbook.write(out);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
            InputStreamResource file = new InputStreamResource(byteArrayInputStream);
            return file;
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Erro ao exportar o arquivo.");
            return null;
        } finally{
            workbook.close();
            out.close();
        }
    }
}
```
</details>

<details>
<summary><b> Implementação do Caso de Uso para Gerar Relatório </b></summary>

Fui responsável por implementar o caso de uso que utiliza o serviço de geração de relatórios para preparar os dados e entregá-los no formato de arquivo Excel.

```java
@Service
public class GerarRelatorioUC {
    private final RelatorioService relatorioService;
    public GerarRelatorioUC(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }
    public InputStreamResource executar(Long idEmpresa) throws IOException {
        List<DadosRelatorio> dadosRelatorio = new ArrayList<DadosRelatorio>();
        return RelatorioService.dataToExcel(dadosRelatorio);
    }
}
```
</details>

<details>
<summary><b> Implementação do Controller para Baixar Relatório </b></summary>

A atividade envolveu criar um controlador que recebe a solicitação para download do relatório e retorna o arquivo Excel gerado.

```java
@RestController
@RequestMapping("api/relatorio")
@RequiredArgsConstructor
public class RelatorioController {
    private final GerarRelatorioUC gerarRelatorioUC;
    private static final String NOME_RELATORIO = "Relatorio.xlsx";
    @GetMapping("{idEmpresa}")
    public ResponseEntity<Resource> download(@PathVariable("idEmpresa") Long idEmpresa) throws IOException {
        InputStreamResource file = gerarRelatorioUC.executar(idEmpresa);
        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + NOME_RELATORIO)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
        return body;
    }
}
```
</details>

#### Hard Skills

- **Java:** Desenvolvi o backend da aplicação utilizando Java, com foco no uso do Spring Framework para criação de APIs RESTful eficientes e seguras.
- **Spring Framework:** Experiência com Spring Boot para criar aplicações web robustas, incluindo segurança e persistência de dados.
- **Maven:** Gerenciei dependências e automatizei processos de build em projetos Java, garantindo consistência e facilidade na integração contínua.
- **Oracle Cloud:** Implementei serviços de nuvem para infraestrutura, escalabilidade e segurança de aplicativos.

#### Soft Skills

- **Resolução de Problemas:** Analisei e diagnostiquei problemas técnicos de forma independente, aplicando soluções criativas e eficazes.
- **Proatividade:** Antecipei necessidades e atuei de forma autônoma para melhorar processos e resolver desafios antes que impactassem o projeto.

</details>

<details>
<summary><b> 2024-2 </b></summary>

A empresa parceira **Pro4Tech** demandou o desenvolvimento de uma plataforma interativa para análise de dados de recrutamento e seleção, com foco na centralização e visualização estratégica de informações. A solução proposta resultou em um sistema que permite importar dados de diferentes fontes e configurar dashboards personalizados com até seis espaços gráficos, incluindo filtros e eixos ajustáveis. O sistema também permite o envio de notificações personalizadas e a exportação de relatórios em formatos Excel e PDF.

[GitHub](https://github.com/api-5-sem)

#### Tecnologias Utilizadas

- **Java:** Linguagem de programação orientada a objetos utilizada no desenvolvimento de sistemas robustos e escaláveis.
- **Spring:** Framework voltado para criação de aplicações web e APIs RESTful com foco em modularidade, segurança e produtividade.
- **TypeScript:** Superset do JavaScript que oferece tipagem estática e maior controle no desenvolvimento frontend.
- **Angular:** Framework para construção de aplicações web modernas, com arquitetura baseada em componentes.
- **PostgreSQL:** Banco de dados relacional, utilizado para garantir integridade, desempenho e escalabilidade no armazenamento de dados.

#### Contribuições Pessoais

<details>
<summary><b> Implementação do serviço para envio de email </b></summary>

Nesta atividade, fui responsável por implementar o serviço de envio de e-mails utilizando o JavaMailSender. A solução permite o envio de mensagens com assunto, corpo e destinatário definidos dinamicamente, sendo utilizada para notificações automatizadas no sistema.

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnviarEmailCommand {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
```

```java
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final String SENDER = "notificacoespro4tech@gmail.com";
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMail(EnviarEmailCommand command){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(SENDER);
            mailMessage.setTo(command.getRecipient());
            mailMessage.setText(command.getMsgBody());
            mailMessage.setSubject(command.getSubject());
            javaMailSender.send(mailMessage);
            log.info("Email enviado com sucesso");
        }
        catch (Exception e) {
            log.info("Erro ao enviar email: {}", e.getCause());
        }
    }
}
```

```java
public interface EmailService {
    void sendSimpleMail(EnviarEmailCommand command);
}

```
</details>

<details>
<summary><b> Implementação do use case e do endpoint para envio de notificações por email </b></summary>

O objetivo da atividade foi implementar o caso de uso que verifica indicadores e aciona o serviço de e-mail. Também foi desenvolvido um endpoint REST para expor essa funcionalidade e permitir sua execução por meio de requisições HTTP.

```java
@Service
@RequiredArgsConstructor
public class NotificacaoUC {

    private final EmailService emailService;
    private final IndicadorRepository indicadorRepository;
    private final IndicadorRepositoryImpl indicadorRepositoryImpl;

    public void verificarIndicadores(String usuario) {

        List<Indicador> indicadores = indicadorRepository.localizarPorUsuario(usuario);

        for (Indicador indicador : indicadores) {
            List<?> resultado = indicadorRepositoryImpl.executarConsultaIndicador(indicador);

            if (!resultado.isEmpty()) {
                enviarEmailNotificacao(indicador);
            }
        }
    }

    private void enviarEmailNotificacao(Indicador indicador) {
        EnviarEmailCommand emailCommand = EnviarEmailCommand.builder()
                .recipient("notificacoespro4tech@gmail.com")
                .subject("Indicador atingido")
                .msgBody("Nome do indicador: " + indicador.getDescricao())
                .build();
        emailService.sendSimpleMail(emailCommand);
    }
}
```

```java
@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificacaoController {

    private final NotificacaoUC notificacaoUC;

    @PostMapping
    public void obterNotificacao(@RequestBody IndicadorCommand indicadorCommand) {
        String usuario = indicadorCommand.getUsuario();
        notificacaoUC.verificarIndicadores(usuario);
    }
}
```

</details>

#### Hard Skills

- **Java/Spring Boot:** Desenvolvi APIs REST seguindo boas práticas, utilizando injeção de dependência e organização em camadas.
- **Integração de APIs:** Implementei e consumi endpoints para garantir a comunicação fluida entre diferentes partes do sistema.
- **Envio de E-mails:** Criei a funcionalidade de envio automático de e-mails personalizados com JavaMailSender.

#### Soft Skills

- **Comunicação:** Mantive contato constante com o time para garantir que os requisitos estivessem sendo atendidos corretamente.
- **Autonomia:** Fui responsável por funcionalidades completas, como a de notificações por e-mail;
- **Organização:** Mantive a estrutura do projeto clara e bem separada em pacotes, facilitando futuras manutenções.


</details>

<details>
<summary><b> 2025-1 </b></summary>

A empresa parceira **Kersys** propôs o desenvolvimento de um sistema inteligente para planejamento e monitoramento de reflorestamento. A solução consistiu em uma plataforma que permite o cadastro e atualização periódica de dados agrícolas, com análise preditiva voltada ao crescimento das plantações e projeção de custos. O sistema utiliza algoritmos de classificação e agrupamento para sugerir melhorias e foi desenvolvido em conformidade com a Lei Geral de Proteção de Dados (LGPD).

[GitHub](https://github.com/api-6-pixel)

#### Tecnologias Utilizadas

- **Java:** Linguagem utilizada para desenvolvimento robusto de aplicações backend.
- **Spring:** Framework completo para construção de aplicações Java, com foco em segurança, injeção de dependência e APIs RESTful.
- **TypeScript:** Superset do JavaScript que adiciona tipagem estática, aumentando a escalabilidade do código frontend.
- **Angular:** Framework moderno para construção de aplicações web dinâmicas e modulares.
- **Python:** Utilizado para desenvolvimento ágil de scripts e APIs com foco em produtividade.
- **FastAPI:** Framework Python para criação rápida e eficiente de APIs REST com validação automática.
- **PostgreSQL:** Banco de dados relacional utilizado para armazenar dados estruturados com segurança e confiabilidade.
- **MongoDB:** Banco de dados NoSQL utilizado para armazenar dados flexíveis em formato JSON.

#### Contribuições Pessoais

<details>
<summary><b> Implementação dos serviços de autenticação e geração de tokens JWT </b></summary>

Nesta atividade, fui responsável por implementar o serviço de autenticação de usuários com Spring Security, utilizando a interface UserDetailsService para recuperar os dados do usuário no login. Além disso, desenvolvi o serviço de geração e validação de tokens JWT, garantindo autenticação segura e compatível a aplicação.

```java
@Service
@RequiredArgsConstructor
public class AutorizacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetails loadUserByUsername(String nomeUsuario) throws UsernameNotFoundException {
        return usuarioRepository.buscarPorNomeUsuario(nomeUsuario);
    }
}
```

```java
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String segredo;

    public String gerarToken(Usuario usuario){
        try{
            Algorithm algoritmo = Algorithm.HMAC256(segredo);
            return JWT.create()
                    .withIssuer("pixel")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(definirDataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro enquanto gera o token", exception);
        }
    }

    public String validarToken(String token){
        try {
            Algorithm algoritmo = Algorithm.HMAC256(segredo);
            return JWT.require(algoritmo)
                    .withIssuer("pixel")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Erro enquanto valida o token", exception);
        }
    }

    private Instant definirDataExpiracao(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
```
</details>

<details>
<summary><b> Implementação do filtro de segurança para autenticação via JWT </b></summary>

Nesta atividade, implementei um filtro personalizado que intercepta as requisições HTTP, extrai o token JWT do cabeçalho, valida o token e define a autenticação no contexto de segurança. A solução foi integrada à cadeia de filtros do Spring Security para proteger as rotas da aplicação.

```java
@Component
@RequiredArgsConstructor
public class FiltroSeguranca extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        var token = this.recuperarToken(request);
        if(token != null){
            var username = tokenService.validarToken(token);
            UserDetails user = usuarioRepository.buscarPorNomeUsuario(username);

            var autenticacao = new UsernamePasswordAuthenticationToken(user, null, null);
            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
```
</details>

<details>
<summary><b> Configuração da segurança da aplicação com Spring Security </b></summary>

O objetivo da atividade foi configurar a segurança da aplicação utilizando o Spring Security. Foram definidos os endpoints públicos e protegidos, o uso de sessão stateless, o filtro de autenticação via JWT, e o codificador de senhas com BCrypt, garantindo controle de acesso e segurança na API.

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ConfiguracaoSeguranca {

    private final FiltroSeguranca filtroSeguranca;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(filtroSeguranca, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
```
</details>

<details>
<summary><b> Implementação da exportação de dados referentes ao lote de um determinado usuário em formato Excel </b></summary>

Nesta atividade, fui responsável por implementar o serviço de exportação dos dados de lote de um determinado usuário no formato .xlsx. Utilizei a biblioteca Apache POI para estruturar e gerar a planilha com informações como nome da fazenda, espécie, status, dados climáticos e de solo, possibilitando o download dos dados em formato tabular.

```java
@Service
public class RelatorioService {

    public static InputStreamResource exportarDadosLoteUsuario(List<DadosRelatorioDTO> dadosRelatorio) throws IOException {

        String[] HEADERS={
                "Nome da Fazenda", "Espécie", "Área Plantada", "Data do Plantio",
                "Custo Esperado", "Status", "Temperatura Ambiente", "Temperatura Solo",
                "Umidade Ambiente", "Umidade Solo", "pH do Solo", "Índice UV", "Data da Atualização"
        };

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream();) {

            Sheet sheet = workbook.createSheet("data");
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < HEADERS.length; i++) {
                headerRow.createCell(i).setCellValue(HEADERS[i]);
            }

            int rowIndex = 1;
            for (DadosRelatorioDTO dto : dadosRelatorio) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(dto.fazendaNome());
                row.createCell(1).setCellValue(dto.especieNome());
                row.createCell(2).setCellValue(dto.areaPlantada());
                row.createCell(3).setCellValue(dto.dataPlantio().toString());
                row.createCell(4).setCellValue(dto.custoEsperado());
                row.createCell(5).setCellValue(dto.status().toString());
                row.createCell(6).setCellValue(dto.temperaturaAmbiente());
                row.createCell(7).setCellValue(dto.temperaturaSolo());
                row.createCell(8).setCellValue(dto.umidadeAmbiente());
                row.createCell(9).setCellValue(dto.umidadeSolo());
                row.createCell(10).setCellValue(dto.phSolo());
                row.createCell(11).setCellValue(dto.indiceUV());
                row.createCell(12).setCellValue(dto.dataAtualizacao().toString());
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

        } catch (IOException e) {
            throw new RuntimeException("Erro ao exportar os dados do lote em .xlsx", e);
        }
    }

    public String gerarNomeArquivo(DadosRelatorioDTO dadosRelatorio) {
        return dadosRelatorio.fazendaNome() + "_" + LocalDate.now() + ".xlsx";
    }
}
```
</details>

<details>
<summary><b> Implementação do caso de uso e endpoint de geração de relatório por plantação </b></summary>

Fui responsável por desenvolver o caso de uso que gera o relatório de uma plantação específica de um usuário. A lógica verifica a existência do usuário, da plantação e da chave associada, e então busca os dados de atualização no banco. Os dados são transformados em um arquivo .xlsx por meio do serviço de relatório. Também implementei o controller REST que expõe o endpoint para download direto do relatório.

```java
@Service
@RequiredArgsConstructor
public class GerarRelatorioUC {

    private final UsuarioRepository usuarioRepository;
    private final AtualizacaoPlantioRepository atualizacaoPlantioRepository;
    private final ChaveUsuarioRepository chaveUsuarioRepository;
    private final RelatorioService relatorioService;

    @Transactional
    public RelatorioDTO executar(Long idUsuario, Long idPlantacao) throws IOException {
        Usuario usuario = usuarioRepository.carregar(idUsuario)
                .orElseThrow(() -> new RuntimeException("Nenhum usuário encontrado para o ID informado."));

        ChaveUsuario chaveUsuario = chaveUsuarioRepository.carregar(idUsuario);
        if (chaveUsuario == null) {
            throw new RuntimeException("Chave do usuário não encontrada para o ID informado.");
        }

        var plantacaoUsuario = usuario.getPlantacao().stream()
                .filter(p -> p.getId().equals(idPlantacao))
                .findFirst();

        if (plantacaoUsuario.isEmpty()) {
            throw new RuntimeException("Plantação não encontrada para o usuário informado.");
        }

        var plantacao = plantacaoUsuario.get();

        List<DadosRelatorioDTO> dadosRelatorio = atualizacaoPlantioRepository.buscarPorPlantacao(plantacao).stream()
                .map(atualizacao -> new DadosRelatorioDTO(
                        plantacao.getFazendaNome(),
                        plantacao.getEspecieNome(),
                        plantacao.getAreaPlantada(),
                        plantacao.getCustoEsperado(),
                        plantacao.getStatus(),
                        plantacao.getDataPlantio(),
                        atualizacao.getTemperaturaAmbiente(),
                        atualizacao.getTemperaturaSolo(),
                        atualizacao.getUmidadeAmbiente(),
                        atualizacao.getUmidadeSolo(),
                        atualizacao.getPhSolo(),
                        atualizacao.getIndiceUV(),
                        atualizacao.getDataRegistro()
                )).toList();

        if (dadosRelatorio.isEmpty()) {
            throw new RuntimeException("Não há dados de atualização para a plantação informada.");
        }

        InputStreamResource relatorio = RelatorioService.exportarDadosLoteUsuario(dadosRelatorio);
        String nomeRelatorio = relatorioService.gerarNomeArquivo(dadosRelatorio.getFirst());

        return new RelatorioDTO(relatorio, nomeRelatorio);
    }
}
```

```java
@RestController
@RequestMapping("/api/relatorio")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final GerarRelatorioUC gerarRelatorioUC;

    @GetMapping("/{idUsuario}/plantacao/{idPlantacao}")
    public ResponseEntity<InputStreamResource> downloadRelatorioFazendaEspecifica(
            @PathVariable Long idUsuario,
            @PathVariable Long idPlantacao) {
        try {
            RelatorioDTO relatorio = gerarRelatorioUC.executar(idUsuario, idPlantacao);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + relatorio.nomeArquivo())
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(relatorio.arquivo());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
```
</details>

#### Hard Skills

- **Java/Spring Boot:** Modelei e desenvolvi toda a lógica do backend utilizando Java com Spring Boot, priorizando segurança, desempenho e uma arquitetura escalável.
- **Spring Security:** Implementei autenticação com JWT e controle de acesso a rotas, garantindo proteção ao sistema.
- **Exportação de Dados (Excel):** Desenvolvi uma funcionalidade para exportar dados de plantios em arquivos .xlsx, facilitando o acompanhamento histórico.

#### Soft Skills

- **Responsabilidade Técnica:** Fui responsável direto por funcionalidades críticas implementadas em Java, como autenticação e exportação de dados.
- **Adaptação:** Naveguei com facilidade entre diferentes tecnologias usando Java como base central do projeto.
- **Pensamento Analítico:** Analisei problemas complexos no código Java e propus soluções eficazes.

</details>

# Danillo Wesley da Costa Silva

## Introdução

Estudante do Curso de Tecnologia em Banco de Dados na Fatec (São José dos Campos/SP).

## Contatos
* [GitHub](https://www.github.com/xxzidanilloxx)
* [LinkedIn](https://www.linkedin.com/in/danillowesley)

## Meus Principais Conhecimentos
- Java
- Spring
- MySQL

## Meus Projetos

### Em 2023-2
O projeto se baseia em um sistema que gerencia a jornada de trabalho dos colaboradores, identifica e classifica as horas extras e sobreavisos, além de gerenciar o fluxo completo desde o registro das horas pelo colaborador até a aprovação pelos gestores.

[GitHub](https://github.com/api-3sem-pixel-api/api)

#### Tecnologias Utilizadas

- Java: linguagem de programação amplamente utilizada para desenvolvimento de aplicativos devido à sua portabilidade, robustez e segurança. Foi fundamental fornecendo uma base sólida e confiável para o projeto.

- Spring: framework para desenvolvimento de aplicativos Java que oferece um conjunto abrangente de ferramentas e bibliotecas para simplificar o desenvolvimento. Ele fornece recursos como injeção de dependência, controle transacional e segurança, ajudando na criação de uma aplicação robusta e de fácil manutenção.

- Maven: ferramenta de automação de compilação e gerenciamento de dependências amplamente utilizada no ecossistema Java. Ele simplifica o processo de construção de projetos e gerenciamento de dependências, garantindo a consistência e reprodutibilidade do ambiente de desenvolvimento, gerenciando as dependências do projeto e automatizando tarefas de compilação e empacotamento.

- VueJS: framework para construção de interfaces de usuário. Ele é conhecido por sua simplicidade e flexibilidade, permitindo o desenvolvimento ágil de interfaces interativas e responsivas, criando uma experiência de usuário moderna e intuitiva no frontend da aplicação.

- MySQL: sistema de gerenciamento de banco de dados relacional que oferece desempenho, confiabilidade e escalabilidade, utilizado para armazenar e gerenciar os dados da aplicação de forma eficiente e segura.

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
- Java: Experiência em desenvolvimento de software utilizando Java, incluindo conhecimento com Programação Orientata à Objetos, herança, polimorfismo, encapsulamento, manipulação de exceções e coleções.

- Spring: Capacidade de desenvolver aplicações com Spring Framework, abrangendo Spring Boot, Spring MVC e Spring Data.

- MySQL: Competência em modelagem de banco de dados relacional utilizando MySQL, incluindo criação de esquemas e consultas SQL complexas.

#### Soft Skills

- Resiliência:
	Mantive uma atitude positiva e persistí diante de obstáculos, buscando soluções criativas e aprendendo com os contratempos para melhorar continuamente meu trabalho na sprint.

- Trabalho em equipe:
	Contribuí de forma construtiva para o trabalho em equipe, comunicando minhas ideias de maneira clara, ouvindo ativamente as contribuições dos outros e sendo receptivo aos feedbacks.

- Adaptabilidade:
	Durante o projeto aprendi novas tecnologias e metodologias, e para isso foi preciso ser capaz de me adaptar rapidamente a essas mudanças. Minha capacidade de se adaptar me permitiu enfrentar novos desafios com confiança e aproveitar as oportunidades de crescimento e aprendizado que surgiram ao longo do projeto.

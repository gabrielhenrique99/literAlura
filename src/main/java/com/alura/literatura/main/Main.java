package com.alura.literatura.main;

import com.alura.literatura.exception.ErroDeConversaoDeAutor;
import com.alura.literatura.exception.ErroDeConversaoDeLivro;
import com.alura.literatura.model.*;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LivroRepository;
import com.alura.literatura.service.ConsumoAPI;
import com.alura.literatura.service.ConverterDados;
import com.alura.literatura.service.IConverteDados;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final IConverteDados converteDados; // Alteração

    public Main(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.converteDados = new ConverterDados(); // Instanciado
    }

    public void exibirMenu() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("""
                    1- Cadastrar livro
                    2- Listar livros cadastrados
                    3- Listar autores cadastrados
                    4- Buscar autores de um determinado ano
                    5- Buscar livros de um determinado idioma
                    0- SAIR
                    """);

            System.out.print("> ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha após o número

            switch (opcao) {
                case 1:
                    cadastrarLivro();
                    break;
                case 2:
                    listarLivrosCadastrados();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresDeUmDeterminadoAno();
                    break;
                case 5:
                    listarLivrosDeUmDeterminadoIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }

    private void cadastrarLivro() {
        System.out.println("Insira o nome do livro para buscar");
        System.out.print("> ");
        String pesquisa = scanner.nextLine().toLowerCase();

        Optional<Livro> livroExiste = livroRepository.findByTitulo(pesquisa);

        if (livroExiste.isPresent()) {
            System.out.println("\nLivro já existe no BD: " + livroExiste.get());
        } else {
            DadosResultado dadosResultado = getDadosLivro(pesquisa);
            if (dadosResultado != null) {
                salvarLivro(dadosResultado);
            } else {
                System.out.println("Não foi possível cadastrar o livro.");
            }
        }
    }

    private DadosResultado getDadosLivro(String pesquisa) {
        System.out.println("Estou fazendo a pesquisa do seu livro...\n");

        String json = ConsumoAPI.obterDados("https://gutendex.com/books?" + "search=" + pesquisa.replace(" ", "+"));
        System.out.println(json);

        return converteDados.obterDados(json, DadosResultado.class); // Alteração aqui
    }

    private void salvarLivro(DadosResultado dadosResultado) {
        System.out.println("Estou inserindo no banco de dados...\n");

        try {
            DadosLivro dadosLivro = dadosResultado.dadosLivro().get(0);
            DadosAutor dadosAutor = dadosLivro.autores().get(0);

            Livro livro = new Livro();
            livro.setTitulo(dadosLivro.titulo());
            livro.setIdioma(dadosLivro.idiomas().get(0));
            livro.setQuantidadeDowloads(dadosLivro.quantidadeDownloads());

            Autor autor = new Autor();
            autor.setNome(dadosAutor.nome());
            autor.setAnoNascimento(dadosAutor.anoNascimento());
            autor.setAnoMorte(dadosAutor.anoMorte());
            autor.setLivro(livro);

            livroRepository.save(livro);
            autorRepository.save(autor);

            System.out.println("\nLivro cadastrado com sucesso!\n");
        } catch (ErroDeConversaoDeLivro | ErroDeConversaoDeAutor e) {
            System.out.println("Houve um erro ao inserir o livro ou o autor no BD: ");
            System.out.println(e.getMessage());
        }
    }

    private void listarLivrosCadastrados() {
        List<Livro> livros = livroRepository.findAll();
        if (!livros.isEmpty()) {
            livros.forEach(livro -> System.out.println("Livro: " + livro));
        } else {
            System.out.println("Sem livros cadastrados.");
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        if (!autores.isEmpty()) {
            autores.forEach(autor -> System.out.println("Autor: " + autor));
        } else {
            System.out.println("Sem autores cadastrados.");
        }
    }

    private void listarAutoresDeUmDeterminadoAno() {
        System.out.println("Insira o ano para buscar autores");
        System.out.print("> ");
        int ano = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        List<Autor> autores = autorRepository.buscarAutoresAte(ano);

        if (!autores.isEmpty()) {
            autores.forEach(autor -> System.out.println("Autor: " + autor));
        } else {
            System.out.println("Não há autores até o ano " + ano);
        }
    }

    private void listarLivrosDeUmDeterminadoIdioma() {
        System.out.println("Insira o idioma (abreviado ou completo)");
        System.out.print("> ");
        String idioma = scanner.nextLine().toLowerCase();

        List<Livro> livros = livroRepository.buscarLivroPorIdioma(idioma);

        if (!livros.isEmpty()) {
            livros.forEach(livro -> System.out.println("Livro: " + livro));
            System.out.println("Quantidade de livros encontrados: " + livros.size());
        } else {
            System.out.println("Não foram encontrados livros no idioma " + idioma);
        }
    }
}

Amazon com SQS


Links: 
    SDK: https://aws.amazon.com/pt/developer/tools/

Dúvidas:
    Dúvida sobre exclusão de mensagens
        - Quando eu deleto a mensagem do SQS eu não posso afetar o processamento de outro consumidores que ainda não receberam? Como isso é controlado internamente? A deleção é feita por consumidos? 

==================================================================================================================================================================


Amazon SQS: Simple Queue Service (Serviço de fila simples)

    - 1 Milhão de requisições gratuitas por mês
    - SQS é um sistema distribuído que prefere ter disponibilidade em detrimento da atualização da informação/dado
    - Sistema autamente escavável e distribuído (por padrão não garante a ordem das mensagens)

==================================================================================================================================================================


Comandos:

    aws configure (informar id, key, região, format)
        - Para configurar um usuário via AWS CLI
    
    aws sqs list-queues
        - Para listar as filas SQS na conta
    
    aws sqs send-message --queue-url https://sqs.us-east-1.amazonaws.com/692820185333/alura-sqs-example --message-body "primeira mensagem enviada para o sqs via AWS CLI na minha vida"
        - Para enviar uma mensagem pra fila SQS
        - Retornar o ID da mensagem
    
    aws sqs receive-message --queue-url https://sqs.us-east-1.amazonaws.com/692820185333/alura-sqs-example

    aws sqs receive-message --queue-url https://sqs.us-east-1.amazonaws.com/692820185333/alura-sqs-example --wait-time-seconds 20s

    aws sqs delete-queue --queue-url https://sqs.us-east-1.amazonaws.com/692820185333/alura-sqs-example

    aws sqs create-queue --queue-name alura-sqs-example

    aws sqs delete-message --queue-url https://sqs.us-east-1.amazonaws.com/692820185333/alura-sqs-example --receipt-handle AQEBpEsxIaXcxysAl7LqWx6nyS7E147+GgVFSHUjaIcK1v/6gC8rjyGadqxCcotEAKrOuCktWlV42hID9ku2WXtuEr4dnCkuYd7FnKpWrznSmT40MY0aAMp//Zz5F/Pt+3UlobX/kohlHCBC9EYHHnkRgjK+xqDn343AeFN+LGRmLpTH/GmdYFgVjfJkSthGkvryWI7N5W0erwwC/XPIqHa3SMCKQjgqsQi+Yr9E7HbMZqcmUG0u6JWue4OusE/qgdfjuogGvlVJ66y0HhIUe12wDLeTbveiXh4I3FIjhd7V08yWPGNrGI7isBO8LqFePshIhtfSEJIu0HvKuxwVvwlDcJM7aaVn9mz0H5yBbtILfRMgP5tAUmehMcGDGuXjC+Qk8QgY/BpHYeTzLYVkdYgeTg==


==================================================================================================================================================================


Aula 01: Conhecendo o SQS

    Nesta aula, aprendemos:

        - O SQS – Simple Queue Service – É o serviço de mensageria da AWS e, nele, podemos enviar e receber mensagens sem provisionar nenhum servidor. 
            Este modelo é altamente escalável e confiável, pois é a própria AWS que cuida da disponibilidade deste serviço.
        - Vimos a importância do desacoplamento de sistemas, em que os consumidores processam mensagens em sua própria disponibilidade e necessidade.
        - Analisamos as vantagens de chamadas assíncronas, que são tolerantes a falhas de rede ou indisponibilidade de sistemas. 
            Chamadas síncronas, por outro lado, exigem 100% de disponibilidade do sistema que está sendo chamado, o que é algo muito difícil (se não impossível) 
            de se atingir quando desenvolvemos na nuvem.
        - Por último, abrimos o console da AWS, selecionamos uma região, buscamos pelo serviço SQS, criamos uma fila e testamos o envio e recebimento de mensagens.

==================================================================================================================================================================


Aula 02: Usando o SQS via AWS-CLI

    Nesta aula, aprendemos:
        - Com o SQS, podemos conectar o nosso ambiente de desenvolvimento local (ou servidores On-Premise) na nuvem, 
            fornecendo as chaves de acesso com as devidas permissões, através do comando aws configure.
        - Operamos nossa fila SQS com os comandos aws sqs create-queue para criar filas, aws sqs delete-queue para excluir uma fila, aws sqs send-message para enviar uma mensagem e aws sqs receive-message para receber as mensagens previamente enviadas.
        - Navegamos, também, na documentação do aws-cli através do comando aws sqs help, que é de grande valia quando esquecemos algum parâmetro ou queremos entender o funcionamento de uma determinada API.

==================================================================================================================================================================

Aula 03: Processamento de mensagens

    Pontos de atenção no CONSUMO e DELEÇÃO
        - É importante excluir as mensagens já processadas após consumir.
        - Para deletar uma mensagem é necessário passar o ReceiptHandle e a URL da fila
        - ReceiptHandle:
            - Controle de recebimento de mensagem, prova que recebi do sqs e tô pronto pra processar
        
        Aplicação Recebe a Mensagem - cerca de 70ms
        Aplicação Processa  a mensagem - (importante saber quanto tempo a aplicação leva aqui, pra configurar o visibility timeout do sqs)
        Aplicação exclui a mensagem - cerca de 70ms

    Visibility TimeOut: 
        - Tempo que a aplicação deve levar pra consumir uma mensagem
        - O ideal é incluir um tempo de visibility timeout maior do que o tempo que a aplicação leva pra processar a mensagem pra não correr nenhum risco
        - A mensagem fica "invisível" frente ao consumidor durante esse tempo, depois ela volta pra fila de consumo
        - Visibility Time Out IDEAL:
            tempo de recebimento (70ms) + tempo de processamento (x -> dende da proposta da app) + tempo de esclusão (70ms)
    
    Short X Long polling

        Para aplicações que podem esperar o consumo
        Ajuda muito a otimizar os cursos do SQS

        Short Polling:
            - Fica fazendo requisições frequentes pra verificar se tem mensagens pra consumir
        
        Long Polling
            - Criar uma conecção até encontrar mensagens novas ou até atingir um tempo limite (máximo de 20s)

    Nesta aula, aprendemos:
        - A processar mensagens de forma segura e eficaz. Abordamos o visibility timeout, que é o tempo que uma mensagem fica invisível para potenciais consumidores, 
            também conhecido como tempo de processamento. O visibility timeout depende de caso a caso e deve ser analisado, com cuidado, pelos times que operam a fila.
        - Que devemos sempre excluir as mensagens que já foram processadas, a fim de evitar duplicidade no processamento. É possível excluir mensagens através do comando 
            aws sqs delete-message, informando a URL da fila e o parâmetro ReceiptHandle.
        - Como o fator custo é muito importante em aplicações na nuvem, olhamos para o conceito de short polling (busca frequente de mensagens), comparamos-o com o 
            long polling (busca infrequente de mensagens) e vimos que podemos economizar muitos recursos quando o long polling é utilizado corretamente.

==================================================================================================================================================================

Aula 04: Tratamento de Erros

    DLQ (Dead Letter Queue)

    Nesta aula, aprendemos:
        - Poison messages podem causar grandes problemas quando não tratadas com o devido cuidado.
        - Imprevistos acontecem e bugs podem ser inseridos a qualquer momento: a troca de assinatura de uma mensagem, a indisponibilidade de sistemas de terceiros 
            ou simplesmente a falha de comunicação entre times pode fazer com que mensagens sejam processadas indefinidamente, caso não possuam uma boa estratégia 
            de tratamento de erros.
        - DLQs são essenciais em qualquer aplicação, pois, com elas, garante-se que, em casos infortúnios, as mensagens sejam direcionadas para um canal específico, 
            em que tanto humanos (processo manual) quanto máquinas (processo automatizado) possam decidir o que fazer com mensagens improcessáveis.
        - Para encaminhar mensagens para DLQs, devemos, primeiro, analisar qual a tolerância de falha de uma determinada mensagem. Para a maioria dos sistemas, 
            3 tentativas é um bom número, mas isto é algo que somente o time responsável pela fila deve decidir, pois os requisitos são os mais diversos em diferentes tipos de sistemas.

==================================================================================================================================================================

Aula 05: Ordem das mensagens 


    SQS FIFO 
        - Garantia de recebimento na ordem que foi publicada, sem duplicidade dos dados (num período de 5m)
        - Limitado a 3k mensagens por segundo 
        - Processa exatamente uma vez 
        - Entrega FIFO
        - É necessário informar um grupo para ordenação (ordenação é baseada em grupos)
    
    Nesta aula, aprendemos:
        - Filas FIFO garantem a ordem de mensagens que possuem o mesmo MessageGroupId.
        - É possível remover mensagens duplicadas – quando enviadas em um intervalo de 300 segundos – ao definir o parâmetro ContentBasedDeduplication como true.
        - Filas FIFO são importantes em cenários onde a ordem das mensagens é importante. Exemplos reais incluem: estoque, transações financeiras, processamento de logs etc.
        - Filas FIFO devem, obrigatoriamente, possuir o sufixo .fifo em sua nomenclatura. A falta deste sufixo impossibilita a criação de filas do tipo FIFO.

==================================================================================================================================================================

Aula 06: Vários Consumidores

    Para ter mais de um consumidor na mesma mensagem você deve ter um TOPIC SNS no producer que direcionar para N SQS consumers

    Nesta aula, aprendemos: 
        - Tópicos desacoplam as inscrições (subscriptions) dos produtores de mensagem (Producers), sendo a única dependência que as aplicações precisam ter o conhecimento.
        - Arquiteturas orientadas e eventos utilizam tópicos agressivamente, pois a funcionalidade de fan-out se aplica constantemente em sistemas de grande demanda.
        - O tópico nativo da AWS chama-se SNS.
        - No SNS, os tipos de inscrições disponíveis são, entre outros: endpoints HTTP, filas SQS e números para envio de SMS.

==================================================================================================================================================================


Aula 07: 

    Nesta aula, aprendemos:
        - A enviar, processar e excluir mensagens utilizando a aws-sdk para Node.js.
        - Utilizar long polling para receber mensagens através da aws-sdk.
        - Que a aws-sdk está disponível em várias linguagens de programação. A lista completa pode ser encontrada no site da AWS.
## Сontexto-ru
### Important!</br>
Both datasets and Python models themselves are missing from this repository. To use this repository or reproduce the application on your computer please visit [Navec](https://github.com/natasha/navec?tab=readme-ov-file) github repository.
### This is a backend part of russian version of game "Contexto", but...

It is not a translator that is used, but a neural network model that vectorizes words, 
which is trained in Russian, and the cosine distance transformed by the formula is used as a similarity metric transformed through the formula
```math
y=e^{9-14\cdot\left(distance\right)}\cdot e^{\left(6*distance\right)}-2.7
```

Additional transformations are performed on this metric so that the similarity values are more understandable to the user, 
since this mathematical function cannot reflect the similarity as qualitatively as possible.

### Tech stack:
- API:
  - Java Spring
  - Spring Data JPA
- Model:
  - Python
  - [Navec](https://github.com/natasha/navec?tab=readme-ov-file) 
  - Numpy
  - Sklearn
- DB:
  - PostgreSQL

### Progress
> Here are the important problems I encountered and the ways to solve them. It will also describe added features that were interesting for me. I am looking forward to criticism from more experienced developers with examples of more productive solutions.

### Working with a Python script through a Spring Boot application

```Java
/**
private String runPythonScript(
            String... args
    ) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add(pythonPath);
        command.add(scriptPath);
        command.addAll(Arrays.asList(args));
        
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Script error, exit code: " + exitCode);
        }
        
        return output.toString();
    }

}
```

### Frontend part.
Visit the [frontend part](https://github.com/n0sebleeded/contexto-ru-frontend) of the application to deploy the project on your local machine!

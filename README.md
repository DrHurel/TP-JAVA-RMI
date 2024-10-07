<p align="center">
  <a href="" rel="noopener">
 <img width=200px height=200px src="https://i.imgur.com/6wj0hh6.jpg" alt="Project logo"></a>
</p>

<h3 align="center">JAVA RMI</h3>


---


## 📝 Table of Contents

- [About](#about)
- [Usage](#usage)

## 🧐 About <a name = "about"></a>

A java rmi project with a client & a server.

### Prerequisites

What things you need to install the software and how to install them.

Python3 : https://www.python.org/downloads/

Gradle : https://gradle.org/install/

Java : version 17 or higher

### Installing


Install
```
gradle build
```



## 🎈 Usage <a name="usage"></a>

Starting the codebase server
```
python3 -m http.server 8000
```

Starting the server
```
gradle :server:run
```


Starting the client

```
gradle :client:run --console plain
```

The client is a CLI that will ask for input to interact with the server.


# PV168 Project: {{ PROJECT NAME }}

{{ PROJECT DESCRIPTION }}

## Team Members:

1. Team Lead:  {{ NAME (EMAIL) }}
2. Member: {{ NAME (EMAIL) }}
3. Member: {{ NAME (EMAIL) }}
4. Member: {{ NAME (EMAIL) }}

- Tech lead (Tutor): {{ TECH LEAD (EMAIL) }}
- Customer: {{ CUSTOMER NAME (EMAIL) }}

## Build And Run Instructions

Clone the repository:

```shell
git clone {{REPO URL}}
```

Go to the project directory

```shell
cd {{PROJECT DIR}}
```

Run the tests:

```shell
mvn test
```

Build the package:

```shell
mvn package
```

The built `.jar` file can be found in the `/target` directory,
you can execute it using:

```shell
java -cp .\target\pv168-project-1.0-SNAPSHOT.jar cz.muni.fi.pv168.project.App
```

Of course, the package name and the ``App`` path might be different for your project
please update this command accordingly

## TODO:

- Update the `README.md` - fill in the project and team details and update the build instructions.
- Update the `pom.xml` see the `FIXME`
- Update the `.gitignore` to ignore unnecessary files (for example IDE dependent)

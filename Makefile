JAVAC = javac
JAR = jar
JAVA = java
JAVAFLAGS = -jar
TARGET = maze
MAIN_CLASS = main.Maze

TEXI2DVI = texi2dvi
PATH_TO_DOC = doc/
INSTALL_DIR = ../install/

SRC_DIR =
DIST_DIR = dist/
PACKAGE_NAME = $(TARGET)
VERSION = 1.0
DIST_SUBDIR = $(PACKAGE_NAME)-$(VERSION)
DIST_ARCHIVE = $(DIST_DIR)$(DIST_SUBDIR).tar.gz

DVI_DEPENDENCIES = $(wildcard $(PATH_TO_DOC)*.texi)

all: check_java_version clean compile install

compile:
	@echo "++++++++++++++++++++++++++++++"
	@echo "ВЫПОЛНЯЕТСЯ СБОРКА ПРОЕКТА С ПОМОЩЬЮ MAVEN"
	mvn -f $(SRC_DIR)pom.xml package
	@echo "СБОРКА ПРОЕКТА С ПОМОЩЬЮ MAVEN ЗАВЕРШЕНА"
	@echo "++++++++++++++++++++++++++++++"

install: compile
	@echo "++++++++++++++++++++++++++++++"
	@echo "ВЫПОЛНЯЕТСЯ УСТАНОВКА: $(TARGET)"
	mkdir -p $(INSTALL_DIR)
	cp $(SRC_DIR)target/$(TARGET)-$(VERSION)-jar-with-dependencies.jar $(INSTALL_DIR)/
	@echo "УСТАНОВКА УСПЕШНО ВЫПОЛНЕНА"
	@echo "++++++++++++++++++++++++++++++"

uninstall:
	@rm -rf $(INSTALL_DIR)

dist: compile
	@echo "++++++++++++++++++++++++++++++"
	@echo "ВЫПОЛНЯЕТСЯ СОЗДАНИЕ АРХИВА: $(DIST_ARCHIVE)"
	@mkdir -p $(DIST_DIR)$(DIST_SUBDIR)
	@cp $(SRC_DIR)target/$(TARGET)-$(VERSION)-jar-with-dependencies.jar $(DIST_DIR)$(DIST_SUBDIR)
	@tar -czf $(DIST_ARCHIVE) -C $(DIST_DIR) $(DIST_SUBDIR)
	@rm -rf $(DIST_DIR)$(DIST_SUBDIR)
	@echo "АРХИВ СОЗДАН УСПЕШНО: $(DIST_ARCHIVE)"
	@echo "++++++++++++++++++++++++++++++"

dvi: $(TARGET).dvi
	@rm -rf $(TARGET).log && rm -rf $(TARGET).toc && rm -rf $(TARGET).aux

test:
	@echo "++++++++++++++++++++++++++++++"
	@echo "ВЫПОЛНЕНИЕ ТЕСТОВ С ПОМОЩЬЮ MAVEN"
	mvn -f $(SRC_DIR)pom.xml test
	@echo "ТЕСТИРОВАНИЕ ЗАВЕРШЕНО"
	@echo "++++++++++++++++++++++++++++++"

maze.dvi: $(DVI_DEPENDENCIES)
	$(TEXI2DVI) $(DVI_DEPENDENCIES)

check_java_version:
	@if ! command -v java > /dev/null; then \
		echo "Java не обнаружена. Установите Java и попробуйте снова."; \
		exit 1; \
	fi
	@echo "Java обнаружена: $$(java -version 2>&1 | head -n 1)"
	@echo

clean:
	rm -rf build
	rm -rf dist
	rm -f $(TARGET).jar
	rm -rf $(SRC_DIR)target
	rm -rf $(TARGET).dvi
	rm -rf $(TARGET_S).dvi

.PHONY: all compile install uninstall dvi dist test clean

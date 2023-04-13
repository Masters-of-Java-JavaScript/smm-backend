up: # For MacOS
	./gradlew clean build && docker compose up -d

upw: # For Windows
	.\gradlew.bat clean build && docker compose up -d

down: # For MacOS
	docker compose down && docker rmi $$(docker images -q smm-backend)

downw: # For Windows
	docker compose down && docker rmi smm-backend:0.0.1

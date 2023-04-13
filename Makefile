up: # For MacOS
	./gradlew clean build && docker compose up -d

upw: # For Windows
	./gradlew.bat clean build && docker compose up -d

down:
	docker compose down && docker rmi $$(docker images -q smm-backend)

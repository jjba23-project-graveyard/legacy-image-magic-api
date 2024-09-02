fmt:
	sbt "scalafixAll; scalafmt"
dev: fmt
	watchexec -r -e scala -- make run
run:
	API_TIMEOUT=3000 API_HOST=localhost API_PORT=8778 sbt "run"

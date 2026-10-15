// tennis-record-k 와 동일 패턴: release 브랜치 푸시 → 빌드 → DockerHub → 홈서버 compose up
def isReleaseBranch() {
  return (env.BRANCH_NAME == 'release' ||
          env.GIT_BRANCH == 'origin/release' ||
          (env.GIT_BRANCH ?: '').endsWith('/release'))
}

pipeline {
  agent any
  options { timestamps() }

  parameters {
    booleanParam(name: 'DEPLOY', defaultValue: true, description: '배포까지 수행할까요?')
  }

  environment {
    DOCKER_IMAGE = 'highjunea37/couponnote-api'
  }

  triggers { pollSCM('H/2 * * * *') }

  stages {
    stage('Checkout') { steps { checkout scm } }

    stage('Build & Test') {
      steps { sh './gradlew clean :api:bootJar :api:test' }
    }

    stage('Docker Build & Push') {
      when { expression { isReleaseBranch() } }
      steps {
        sh """
          docker build --platform linux/amd64 -t ${DOCKER_IMAGE}:latest -f api/Dockerfile api
          docker push ${DOCKER_IMAGE}:latest
        """
      }
    }

    stage('Deploy') {
      when { expression { isReleaseBranch() && params.DEPLOY } }
      steps {
        sh '''
          cd /home/highjune/services/couponnote
          docker compose pull
          docker compose up -d
        '''
      }
    }
  }

  post {
    success { echo "✅ ${env.GIT_BRANCH ?: env.BRANCH_NAME} 완료" }
    failure { echo "❌ 실패: 로그 확인" }
  }
}

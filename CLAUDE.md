# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 아키텍처

### 모듈 구조 (Clean Architecture)
프로젝트는 명확한 의존성 흐름을 가진 계층형 모듈 아키텍처를 사용합니다:

```
core (기반 계층, 의존성 없음)
  ↑
data (레포지토리, API 클라이언트)
  ↑
presentation (UI, ViewModel, Compose 화면)
  ↑
app (Android 애플리케이션 진입점)
```

**모듈별 역할:**
- `core`: 공유 도메인 모델, 유틸리티, 시리얼라이저. Android 의존성 없는 순수 Kotlin.
- `data`: 레포지토리 구현체, 네트워크 계층 (Retrofit + OkHttp 준비됨), 데이터 소스.
- `presentation`: Jetpack Compose UI, ViewModel, 네비게이션, 디자인 시스템 컴포넌트.
- `app`: Application 클래스, MainActivity, 모듈 통합.

### 주요 아키텍처 패턴

**Intent 기반 MVVM:**
- ViewModel은 `onIntent(intent: XIntent)` 함수를 노출
- UI 화면은 sealed interface Intent 객체를 발행
- 예시: `HomeIntent.ClickCalendar` → `HomeViewModel.onIntent()` → 네비게이션 처리

**Side Effect Bus 패턴:**
- `MoaSideEffectBus`는 앱 전역 이벤트를 위한 싱글톤 `SharedFlow<MoaSideEffect>`
- 네비게이션(`MoaSideEffect.Navigate`)과 다이얼로그(`MoaSideEffect.Dialog`)에 사용
- `MainViewModel`에서 수집되어 중앙 집중식으로 처리
- 이벤트 발행을 위해 Hilt를 통해 ViewModel에 주입됨

**네비게이션 시스템 (Navigation3):**
- 3개의 네비게이션 계층: `RootNavigation`, `OnboardingNavigation`, `SettingNavigation`
- 모두 object/data class 목적지를 가진 sealed interface
- 네비게이션 인자는 assisted injection을 통해 ViewModel로 전달
- 예시: `NickNameViewModel`은 `@AssistedInject`를 통해 `NicknameNavigationArgs`를 받음

**Repository 패턴:**
- data 모듈에 인터페이스 정의 (예: `OnboardingRepository`)
- Hilt의 `RepositoryModule`을 통해 구현체 바인딩
- 현재는 목 데이터 반환, 네트워크 통합 준비 완료

### 의존성 주입 (Hilt)

**DI 모듈:**
- `presentation/di/BusModule.kt`: 싱글톤 `MoaSideEffectBus` 제공
- `data/di/RepositoryModule.kt`: 레포지토리 인터페이스를 구현체에 바인딩

**ViewModel 주입 패턴:**
```kotlin
// 표준 ViewModel
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel()

// 네비게이션 인자를 받는 ViewModel (assisted injection)
@HiltViewModel(assistedFactory = NickNameViewModel.Factory::class)
class NickNameViewModel @AssistedInject constructor(
    @Assisted private val args: NicknameNavigationArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val onboardingRepository: OnboardingRepository,
) : ViewModel()
```

### 디자인 시스템 (Jetpack Compose)

**테마 시스템:**
- Composition Local을 통한 커스텀 테마: `LocalMoaColors`, `LocalMoaTypography`, `LocalMoaRadius`, `LocalMoaSpacing`
- `MoaTheme.colors`, `MoaTheme.typography` 등을 통해 접근
- `presentation/designsystem/theme/`에 위치

**재사용 가능한 컴포넌트:**
- 모든 커스텀 컴포넌트는 `presentation/designsystem/component/`에 위치
- 포함: `MoaButton`, `MoaTextField`, `MoaDialog`, `MoaBottomSheet`, `MoaTopAppBar`
- 유효성 검증을 위한 입력 변환: `SalaryInputTransformation`, `CurrencyOutputTransformation`

**화면 구조:**
```
presentation/ui/
├── MainActivity.kt              # 루트 진입점, NavHost 설정
├── MainViewModel.kt             # 중앙 네비게이션/다이얼로그 오케스트레이션
├── splash/                      # 스플래시 화면
├── onboarding/                  # 다단계 온보딩 플로우
│   ├── login/, nickname/, workplace/, salary/, workschedule/, widgetguide/
├── home/                        # 홈 화면
├── history/                     # 히스토리 화면
├── setting/                     # 설정 플로우
└── webview/                     # WebView 래퍼
```

### Core 모델

**`core/model/`의 주요 도메인 모델:**
- `OnboardingState`: 온보딩 단계의 완료 상태 추적
- `SalaryType`: 열거형 (월급, 연봉)
- `WorkScheduleDay`: 한글 제목이 있는 요일, 직렬화 가능
- `Time`: Sealed interface (Work/Lunch), 시간 범위 포맷팅 기능
- `Terms`: 약관 데이터 모델
- `ImmutableListSerializer`: kotlinx immutable collections용 커스텀 시리얼라이저

### 기술 스택

**언어 & 빌드:**
- Kotlin 2.2.21 (Java 21 호환)
- Gradle 8.12.3, KSP 2.3.3 (어노테이션 처리)
- Compose Compiler (stable marker, `@Stable` 어노테이션)

**Android:**
- minSdk 26 (Android 8.0+), targetSdk 36
- Jetpack Compose BOM 2026.01.00
- Navigation 3 (1.0.0) with Hilt 통합
- Hilt 2.57.2 (의존성 주입)

**데이터 계층 (사용 준비 완료):**
- Retrofit 3.0.0 + OkHttp 5.3.2
- Kotlinx Serialization 1.9.0
- Kotlinx Collections Immutable 0.4.0

**상태 관리:**
- Kotlin Coroutines 1.10.2
- StateFlow/SharedFlow (반응형 상태)
- `collectAsStateWithLifecycle()` (Compose 통합)

### 패키지 명명 규칙

모든 패키지는 `com.moa.app.[module].[layer].[domain]` 규칙을 따름

예시:
- `com.moa.app.presentation.ui.home`
- `com.moa.app.presentation.designsystem.component`
- `com.moa.app.data.repository`
- `com.moa.app.core.model`

### 앱 설정

**버전 관리:**
- 버전 코드/이름은 `gradle.properties`에 정의
- `APP_VERSION_CODE=1`, `APP_VERSION_NAME=1.0.0`

**릴리즈 빌드:**
- 릴리즈 빌드 시 ProGuard 활성화
- 리소스 축소 활성화
- ProGuard 규칙은 `app/proguard-rules.pro`에 위치

## 개발 워크플로우

**새 화면 추가 시:**
1. 적절한 `Navigation` 클래스에 sealed interface 목적지 생성
2. `presentation/ui/[feature]/`에 화면 Composable 생성
3. `@HiltViewModel`을 사용한 ViewModel 생성 (인자가 필요하면 assisted injection 사용)
4. 사용자 액션을 위한 Intent sealed interface 정의
5. `MoaSideEffectBus`를 통해 네비게이션 이벤트 발행
6. `MainScreen.kt`에 네비게이션 라우트 핸들링 추가

**새 레포지토리 추가 시:**
1. `data/repository/`에 인터페이스 정의
2. 구현 클래스 생성
3. `data/di/RepositoryModule.kt`에 바인딩
4. 필요한 ViewModel에 주입

**네트워크 호출 추가 시:**
1. Retrofit 어노테이션으로 API 인터페이스 정의
2. `@Serializable`로 데이터 모델 생성
3. 레포지토리 계층에 구현
4. ViewModel에서 로딩/에러 상태 처리

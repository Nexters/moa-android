# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 아키텍처

### 모듈 구조 (Clean Architecture)
프로젝트는 명확한 의존성 흐름을 가진 계층형 모듈 아키텍처를 사용합니다:

```
core (기반 계층, 의존성 없음)
  ↓ depends on
data (레포지토리, API 클라이언트)
  ↓ depends on
presentation (UI, ViewModel, Compose 화면)
  ↓ depends on
app (Android 애플리케이션 진입점)
```

**모듈별 역할:**
- `core`: 공유 도메인 모델, 유틸리티, 시리얼라이저. Android 의존성 없는 순수 Kotlin.
- `data`: 레포지토리 구현체, 네트워크 계층 (Retrofit + OkHttp), DataStore, 보안(Tink).
- `presentation`: Jetpack Compose UI, ViewModel, 네비게이션, 디자인 시스템 컴포넌트, Glance 위젯.
- `app`: Application 클래스, MainActivity, Hilt 통합, Firebase/Kakao SDK 초기화.

### 주요 아키텍처 패턴

**Intent 기반 MVI:**
- ViewModel은 `onIntent(intent: XIntent)` 함수를 노출
- UI 화면은 sealed interface Intent 객체를 발행
- 예시: `NotificationSettingIntent.ToggleNotification(id, enabled)` → `NotificationSettingViewModel.onIntent()` → Repository 호출

**Side Effect Bus 패턴:**
- `MoaSideEffectBus`는 앱 전역 이벤트를 위한 싱글톤 `SharedFlow<MoaSideEffect>`
- `MoaSideEffect` 종류:
  - `Navigate(destination: RootNavigation)` - 화면 전환
  - `Dialog(dialog: MoaDialogProperties?)` - 다이얼로그 표시/숨김
  - `Loading(isLoading: Boolean)` - 로딩 상태
  - `Failure(exception: Throwable)` - 에러 처리
- `MainViewModel`에서 수집되어 중앙 집중식으로 처리
- Hilt를 통해 ViewModel에 주입됨

**Suspend Extension 패턴:**
```kotlin
suspend { repository.someOperation() }.execute(
    bus = moaSideEffectBus,
    scope = viewModelScope,
) { result ->
    // success handler
}
```
자동으로 Loading/Failure side effect 발행 처리.

**네비게이션 시스템 (Navigation3):**
- 3개의 네비게이션 계층: `RootNavigation`, `OnboardingNavigation`, `SettingNavigation`
- 모두 `@Serializable` object/data class 목적지를 가진 sealed interface
- 네비게이션 인자는 assisted injection을 통해 ViewModel로 전달
- 예시:
```kotlin
@HiltViewModel(assistedFactory = NickNameViewModel.Factory::class)
class NickNameViewModel @AssistedInject constructor(
    @Assisted private val args: OnboardingNavigation.Nickname.NicknameNavigationArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val onboardingRepository: OnboardingRepository,
) : ViewModel()
```

**Repository 패턴:**
- data 모듈에 인터페이스 정의 (`OnboardingRepository`, `SettingRepository`, `TokenRepository`)
- Hilt의 `RepositoryModule`을 통해 구현체 바인딩
- 현재는 목 데이터 반환, 네트워크 통합 준비 완료

### 의존성 주입 (Hilt)

**DI 모듈:**
- `presentation/di/BusModule.kt`: 싱글톤 `MoaSideEffectBus` 제공
- `data/di/RepositoryModule.kt`: 레포지토리 인터페이스를 구현체에 바인딩
- `data/di/SecurityModule.kt`: Tink 암호화, 보안 유틸리티 제공

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
    @Assisted private val args: OnboardingNavigation.Nickname.NicknameNavigationArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(args: OnboardingNavigation.Nickname.NicknameNavigationArgs): NickNameViewModel
    }
}
```

## 주요 패턴 및 컨벤션

**State 관리:**
- ViewModel에서 `StateFlow<UiState>` 패턴 사용
- UI State는 data class로 정의
- `_uiState.update { it.copy(...) }` 패턴으로 불변 업데이트

**에러 처리:**
- `MoaSideEffect.Failure(exception)` 발행
- `MainViewModel`에서 중앙 집중식 처리
- 사용자에게 에러 다이얼로그 표시

**로딩 상태:**
- `MoaSideEffect.Loading(isLoading)` 발행
- `MainViewModel`에서 전역 로딩 인디케이터 제어

**다이얼로그:**
- `MoaSideEffect.Dialog(MoaDialogProperties(...))` 발행
- Alert/Confirm 타입 지원
- 버튼 텍스트/색상 커스터마이징 가능

**네비게이션:**
- `MoaSideEffect.Navigate(destination)` 발행
- `MainViewModel`에서 NavController 제어
- 백 스택 관리 로직 포함

**불변 컬렉션:**
- UI State에 `ImmutableList<T>` 사용
- `persistentListOf()`, `.toImmutableList()` 활용
- Compose 리컴포지션 최적화

**시리얼라이제이션:**
- 모든 네트워크/저장 모델에 `@Serializable` 추가
- Navigation args에도 `@Serializable` 필수
- 커스텀 시리얼라이저: `ImmutableListSerializer`


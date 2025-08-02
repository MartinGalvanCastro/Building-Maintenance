# AuthenticationApi

All URIs are relative to *http://localhost:8080/api*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**login**](#login) | **POST** /auth/login | Authenticate user and return JWT token|
|[**logout**](#logout) | **POST** /auth/logout | Logout user|
|[**validateToken**](#validatetoken) | **GET** /auth/validate | Check if a JWT token is valid|

# **login**
> LogInResultDto login(credentialsDto)

Authenticates a user with email and password. Returns a JWT token if successful.

### Example

```typescript
import {
    AuthenticationApi,
    Configuration,
    CredentialsDto
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthenticationApi(configuration);

let credentialsDto: CredentialsDto; //User credentials

const { status, data } = await apiInstance.login(
    credentialsDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **credentialsDto** | **CredentialsDto**| User credentials | |


### Return type

**LogInResultDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Successful authentication, returns JWT token |  -  |
|**400** | Invalid request body or validation error |  -  |
|**401** | Invalid credentials |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **logout**
> logout()

Logs out the user by invalidating the JWT token.

### Example

```typescript
import {
    AuthenticationApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthenticationApi(configuration);

const { status, data } = await apiInstance.logout();
```

### Parameters
This endpoint does not have any parameters.


### Return type

void (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Logout successful |  -  |
|**400** | Missing or malformed Authorization header |  -  |
|**401** | Invalid or expired token |  -  |
|**403** | User not authorized to perform this action |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **validateToken**
> validateToken()

Returns 200 if the provided JWT token is valid, 401 if not.

### Example

```typescript
import {
    AuthenticationApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthenticationApi(configuration);

let authorization: string; // (default to undefined)

const { status, data } = await apiInstance.validateToken(
    authorization
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **authorization** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Token is valid |  -  |
|**401** | Token is invalid or expired |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)


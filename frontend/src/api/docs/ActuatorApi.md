# ActuatorApi

All URIs are relative to *http://localhost:8080/api*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**health**](#health) | **GET** /actuator/health | Actuator web endpoint \&#39;health\&#39;|

# **health**
> object health()


### Example

```typescript
import {
    ActuatorApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ActuatorApi(configuration);

const { status, data } = await apiInstance.health();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/vnd.spring-boot.actuator.v3+json, application/vnd.spring-boot.actuator.v2+json, application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)


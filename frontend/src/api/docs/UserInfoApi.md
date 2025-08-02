# UserInfoApi

All URIs are relative to *http://localhost:8080/api*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getCurrentUser**](#getcurrentuser) | **GET** /me | Get current user info|

# **getCurrentUser**
> UserInfoDto getCurrentUser()

Returns information about the currently authenticated user, including their role, relationships, and related maintenance requests.

### Example

```typescript
import {
    UserInfoApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UserInfoApi(configuration);

const { status, data } = await apiInstance.getCurrentUser();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**UserInfoDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | User info returned successfully |  -  |
|**401** | Unauthorized - Invalid or missing token |  -  |
|**404** | User not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)


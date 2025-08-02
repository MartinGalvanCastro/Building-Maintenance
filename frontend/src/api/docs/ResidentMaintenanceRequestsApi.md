# ResidentMaintenanceRequestsApi

All URIs are relative to *http://localhost:8080/api*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**cancel**](#cancel) | **DELETE** /residents/{residentId}/requests/{requestId} | Cancel a maintenance request for a resident|
|[**create2**](#create2) | **POST** /residents/{residentId}/requests | Create a new maintenance request for a resident|
|[**get**](#get) | **GET** /residents/{residentId}/requests/{requestId} | Get a maintenance request by ID for a resident|
|[**list**](#list) | **GET** /residents/{residentId}/requests | List all maintenance requests for a resident|
|[**update4**](#update4) | **PATCH** /residents/{residentId}/requests/{requestId} | Update a maintenance request for a resident|

# **cancel**
> CancelResponseDto cancel()

Cancels a maintenance request for the specified resident.

### Example

```typescript
import {
    ResidentMaintenanceRequestsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentMaintenanceRequestsApi(configuration);

let residentId: string; //UUID of the resident (default to undefined)
let requestId: string; //UUID of the maintenance request (default to undefined)

const { status, data } = await apiInstance.cancel(
    residentId,
    requestId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **residentId** | [**string**] | UUID of the resident | defaults to undefined|
| **requestId** | [**string**] | UUID of the maintenance request | defaults to undefined|


### Return type

**CancelResponseDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Maintenance request cancelled |  -  |
|**404** | Maintenance request not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create2**
> MaintenanceRequestDto create2(createRequestDto)

Creates a new maintenance request for the specified resident.

### Example

```typescript
import {
    ResidentMaintenanceRequestsApi,
    Configuration,
    CreateRequestDto
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentMaintenanceRequestsApi(configuration);

let residentId: string; //UUID of the resident (default to undefined)
let createRequestDto: CreateRequestDto; //Maintenance request creation data

const { status, data } = await apiInstance.create2(
    residentId,
    createRequestDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createRequestDto** | **CreateRequestDto**| Maintenance request creation data | |
| **residentId** | [**string**] | UUID of the resident | defaults to undefined|


### Return type

**MaintenanceRequestDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | Maintenance request created |  -  |
|**400** | Invalid input data |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get**
> MaintenanceRequestDto get()

Returns a maintenance request by its unique identifier for the specified resident.

### Example

```typescript
import {
    ResidentMaintenanceRequestsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentMaintenanceRequestsApi(configuration);

let residentId: string; //UUID of the resident (default to undefined)
let requestId: string; //UUID of the maintenance request (default to undefined)

const { status, data } = await apiInstance.get(
    residentId,
    requestId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **residentId** | [**string**] | UUID of the resident | defaults to undefined|
| **requestId** | [**string**] | UUID of the maintenance request | defaults to undefined|


### Return type

**MaintenanceRequestDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Maintenance request found |  -  |
|**404** | Maintenance request not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **list**
> Array<MaintenanceRequestDto> list()

Returns a list of all maintenance requests for the specified resident.

### Example

```typescript
import {
    ResidentMaintenanceRequestsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentMaintenanceRequestsApi(configuration);

let residentId: string; //UUID of the resident (default to undefined)

const { status, data } = await apiInstance.list(
    residentId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **residentId** | [**string**] | UUID of the resident | defaults to undefined|


### Return type

**Array<MaintenanceRequestDto>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | List of maintenance requests |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update4**
> MaintenanceRequestDto update4(updateRequestDto)

Updates an existing maintenance request for the specified resident.

### Example

```typescript
import {
    ResidentMaintenanceRequestsApi,
    Configuration,
    UpdateRequestDto
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentMaintenanceRequestsApi(configuration);

let residentId: string; //UUID of the resident (default to undefined)
let requestId: string; //UUID of the maintenance request (default to undefined)
let updateRequestDto: UpdateRequestDto; //Maintenance request update data

const { status, data } = await apiInstance.update4(
    residentId,
    requestId,
    updateRequestDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateRequestDto** | **UpdateRequestDto**| Maintenance request update data | |
| **residentId** | [**string**] | UUID of the resident | defaults to undefined|
| **requestId** | [**string**] | UUID of the maintenance request | defaults to undefined|


### Return type

**MaintenanceRequestDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Maintenance request updated |  -  |
|**400** | Invalid input data |  -  |
|**404** | Maintenance request not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

